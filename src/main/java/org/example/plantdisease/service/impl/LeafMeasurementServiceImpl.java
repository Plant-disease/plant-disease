package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.Attachment;
import org.example.plantdisease.entity.LeafData;
import org.example.plantdisease.entity.LeafMeasurement;
import org.example.plantdisease.payload.VeinFeatures;
import org.example.plantdisease.service.ImageProcessing;
import org.example.plantdisease.service.LeafMeasurementService;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafMeasurementServiceImpl implements LeafMeasurementService {

    @Value("${attachment.second.package.url}")
    String packageUrl;

    private final ImageProcessing imageProcessing;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public List<LeafMeasurement> getLeafMeasurementToSaveDB(LeafData leafData, List<Attachment> attachmentList) {

        List<LeafMeasurement> leafMeasurementList = new ArrayList<>();

        for (Attachment attachment : attachmentList) {

            LeafMeasurement leafMeasurement = getLeafMeasurement(attachment);
            leafMeasurement.setLeafData(leafData);
            leafMeasurementList.add(leafMeasurement);
        }

        return leafMeasurementList;
    }

    @Override
    public List<LeafMeasurement> getLeafMeasurementToCheckKNN(List<Attachment> attachmentList) {

        List<LeafMeasurement> leafMeasurementList = new ArrayList<>();

        for (Attachment attachment : attachmentList) {

            leafMeasurementList.add(getLeafMeasurement(attachment));
        }

        return leafMeasurementList;
    }

    public LeafMeasurement getLeafMeasurement(Attachment attachment) {

        String imagePath = attachment.getUrl();
        LeafMeasurement leafMeasurement = new LeafMeasurement();

        VeinFeatures veinFeatures = getVeinFeature(imagePath);
        leafMeasurement.setLeafArea(getLeafArea(imagePath));
        leafMeasurement.setLeafPerimeter(getLeafPerimeter(imagePath));
        leafMeasurement.setCircularity(getLeafCircularity(imagePath));
        leafMeasurement.setElongation(getElongation(imagePath));
        leafMeasurement.setCompactness(getCompactness(imagePath));
        leafMeasurement.setLeafAspectRatio(getLeafAspectRatio(imagePath));
        leafMeasurement.setTextureEnergy(getTextureEnergy(imagePath));
        leafMeasurement.setTotalVeinCount(veinFeatures.getTotalVeinCount());
        leafMeasurement.setAverageVeinWidth(veinFeatures.getAverageVeinWidth());
        leafMeasurement.setAverageVeinLength(veinFeatures.getAverageVeinLength());
        leafMeasurement.setFractalDimension(getFractalDimension(imagePath));

        System.out.println(imagePath);
        System.out.println("LeafArea: " + leafMeasurement.getLeafArea());
        System.out.println("LeafPerimeter: " + leafMeasurement.getLeafPerimeter());
        System.out.println("Circularity: " + leafMeasurement.getCircularity());
        System.out.println("Elongation: " + leafMeasurement.getElongation());
        System.out.println("Compactness: " + leafMeasurement.getCompactness());
        System.out.println("LeafAspectRatio: " + leafMeasurement.getLeafAspectRatio());
        System.out.println("TextureEnergy: " + leafMeasurement.getTextureEnergy());
        System.out.println("TotalVeinCount: " + leafMeasurement.getTotalVeinCount());
        System.out.println("AverageVeinWidth: " + leafMeasurement.getAverageVeinWidth());
        System.out.println("AverageVeinLength: " + leafMeasurement.getAverageVeinLength());
        System.out.println("FractalDimension: " + leafMeasurement.getFractalDimension());
//        throw new RestException("COULD_NOT_GET_LEAF_MEASUREMENTS_FROM_IMAGE", HttpStatus.INTERNAL_SERVER_ERROR);

        return leafMeasurement;
    }


    public double getLeafArea(String attachmentURL) {

        try {

            String path = imageProcessing.getWhiteImage(attachmentURL);
            // Load the image
            Mat image = Imgcodecs.imread(path);

            // Convert the image to grayscale
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

            // Threshold the image to create a binary image
            Mat binaryImage = new Mat();
            Imgproc.threshold(grayImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY);

            // Find contours in the binary image
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Find the contour with maximum area (assuming it's the leaf)
            double maxArea = -1;
            int maxAreaContourIndex = -1;
            for (int i = 0; i < contours.size(); i++) {
                double area = Imgproc.contourArea(contours.get(i));
                if (area > maxArea) {
                    maxArea = area;
                    maxAreaContourIndex = i;
                }
            }

            // Calculate the area of the detected contour (leaf area)
            double leafArea = Imgproc.contourArea(contours.get(maxAreaContourIndex));

            return leafArea;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getElongation(String attachmentURL) {

        try {

            String path = imageProcessing.getWhiteImage(attachmentURL);

            // Load the image
            BufferedImage image = loadImage(path);

            // Fit an ellipse to the leaf
            Ellipse2D.Double ellipse = fitEllipse(image);

            // Get the major and minor axis lengths
            double majorAxisLength = Math.max(ellipse.width, ellipse.height);
            double minorAxisLength = Math.min(ellipse.width, ellipse.height);

            // Calculate the elongation (major axis length / minor axis length)
            return majorAxisLength / minorAxisLength;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getCompactness(String attachmentURL) {
        try {

            String path = imageProcessing.getWhiteImage(attachmentURL);

            // Load the image
            BufferedImage image = loadImage(path);

            // Calculate the area of the leaf
            double area = getLeafArea(image);

            // Calculate the convex hull area
            double convexArea = getConvexHullArea(image);

            // Calculate the compactness (area / convex area)
            return area / convexArea;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getLeafPerimeter(String attachmentURL) {
        try {

            // Load OpenCV library
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            // Load the image
            BufferedImage path = loadImage(attachmentURL);

            // Process the image
            BufferedImage processedImage = processImage(path);

            String imagePath = packageUrl + "\\image_of_leaf_perimeter.jpg";
            saveImage(processedImage, imagePath);

            Mat image = Imgcodecs.imread(imagePath);

            // Convert the image to grayscale
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

            // Threshold the image to create a binary image
            Mat binaryImage = new Mat();
            Imgproc.threshold(grayImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY);

            // Find contours in the binary image
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Find the contour with maximum area (assuming it's the leaf)
            double maxArea = -1;
            int maxAreaContourIndex = -1;
            for (int i = 0; i < contours.size(); i++) {
                double area = Imgproc.contourArea(contours.get(i));
                if (area > maxArea) {
                    maxArea = area;
                    maxAreaContourIndex = i;
                }
            }
            // Calculate the perimeter of the detected contour

            return Imgproc.arcLength(new MatOfPoint2f(contours.get(maxAreaContourIndex).toArray()), true);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getTextureEnergy(String attachmentURL) {
        try {

            String path = imageProcessing.getWhiteImage(attachmentURL);

            // Load the image
            BufferedImage image = loadImage(path);

            // Get the leaf region
            BufferedImage leafRegion = getLeafRegion(image);

            // Calculate the average intensity of the leaf region
            double sum = 0;
            int count = 0;
            for (int y = 0; y < leafRegion.getHeight(); y++) {
                for (int x = 0; x < leafRegion.getWidth(); x++) {
                    int rgb = leafRegion.getRGB(x, y);
                    Color color = new Color(rgb);
                    int intensity = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    sum += intensity;
                    count++;
                }
            }
            double averageIntensity = sum / count;

            // Calculate the variance of pixel intensities within the leaf region
            double variance = 0;
            for (int y = 0; y < leafRegion.getHeight(); y++) {
                for (int x = 0; x < leafRegion.getWidth(); x++) {
                    int rgb = leafRegion.getRGB(x, y);
                    Color color = new Color(rgb);
                    int intensity = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    variance += Math.pow(intensity - averageIntensity, 2);
                }
            }
            variance /= count;

            return variance;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getLeafCircularity(String attachmentURL) {

        try {
            String path = imageProcessing.getWhiteImage(attachmentURL);

            // Load the image
            BufferedImage image = loadImage(path);

            // Calculate the area and perimeter of the leaf
            double area = getLeafArea(image);
            double perimeter = getLeafPerimeter(image);

            // Calculate the circularity
            return 4 * Math.PI * area / (perimeter * perimeter);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public double getLeafAspectRatio(String attachmentURL) {

        try {

            String path = imageProcessing.getWhiteImage(attachmentURL);

            // Load the image
            BufferedImage image = loadImage(path);


            // Get the bounding box of the leaf
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;

            // Iterate through each pixel in the image
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    if (rgb == -1) { // White color
                        // Update bounding box coordinates
                        minX = Math.min(minX, x);
                        minY = Math.min(minY, y);
                        maxX = Math.max(maxX, x);
                        maxY = Math.max(maxY, y);
                    }
                }
            }

            // Calculate the width and height of the bounding box
            int width = maxX - minX;
            int height = maxY - minY;

            // Calculate the aspect ratio (width / height)
            return (double) width / height;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getFractalDimension(String attachmentURL) {
        try {
            // Load the leaf image
            BufferedImage image = ImageIO.read(new File(attachmentURL));

            // Convert the image to binary (black and white)
            int width = image.getWidth();
            int height = image.getHeight();
            boolean[][] binaryImage = new boolean[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    // Convert RGB to grayscale
                    int gray = (int) (0.2989 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                    // Convert grayscale to binary
                    binaryImage[y][x] = gray < 128; // Assuming threshold value of 128
                }
            }

            // Calculate the fractal dimension
            return calculateFractalDimension(binaryImage);

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public VeinFeatures getVeinFeature(String attachmentURL) {

        try {

            // OpenCV kutubxonasini yuklash
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            // Rasmi yuklash
            Mat inputImage = Imgcodecs.imread(attachmentURL);

            // Convert image to grayscale
            Mat grayImage = new Mat();
            Imgproc.cvtColor(inputImage, grayImage, Imgproc.COLOR_BGR2GRAY);

            // Apply thresholding to extract veins
            Mat binaryImage = new Mat();
            Imgproc.threshold(grayImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

            // Find contours
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Calculate vein features
            double totalVeinLength = 0;
            double totalVeinWidth = 0;
            int totalVeinCount = contours.size();

            for (MatOfPoint contour : contours) {
                // Calculate vein length for each contour
                double veinLength = Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true);
                totalVeinLength += veinLength;

                // Calculate bounding rectangle for each contour
                Rect boundingRect = Imgproc.boundingRect(contour);

                // Estimate vein width (considering the width of the bounding rectangle)
                double veinWidth = boundingRect.width;
                totalVeinWidth += veinWidth;
            }

            // Calculate average vein length and width
            double avgVeinLength = totalVeinLength / totalVeinCount;
            double avgVeinWidth = totalVeinWidth / totalVeinCount;

            VeinFeatures veinFeatures = new VeinFeatures();
            veinFeatures.setTotalVeinCount(totalVeinCount);
            veinFeatures.setAverageVeinLength(avgVeinLength);
            veinFeatures.setAverageVeinWidth(avgVeinWidth);

            return veinFeatures;
        } catch (Exception e) {
            e.printStackTrace();
            return new VeinFeatures(0, 0, 0);
        }
    }


    private static boolean isLeafPixel(Color color) {
        // Threshold values for detecting green color of the leaf
        int greenThreshold = 100;

        // Check if the color is close to green (leaf color)
        return color.getRed() < greenThreshold && color.getGreen() > greenThreshold && color.getBlue() < greenThreshold;
    }

    private static double getLeafArea(BufferedImage image) {
        int leafArea = 0;

        // Count the white pixels (leaf area)
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                if (rgb == Color.WHITE.getRGB()) {
                    leafArea++;
                }
            }
        }

        return leafArea;
    }

    private static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static double getLeafPerimeter(BufferedImage image) {
        int perimeter = 0;

        // Count the pixels on the perimeter
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (isPerimeterPixel(image, x, y)) {
                    perimeter++;
                }
            }
        }

        return perimeter;
    }

    private static double getConvexHullArea(BufferedImage image) {
        // Find the convex hull of the leaf
        List<java.awt.Point> leafPoints = findLeafPoints(image);
        List<java.awt.Point> convexHullPoints = findConvexHull(leafPoints);

        // Calculate the area of the convex hull using the shoelace formula
        double area = 0;
        int n = convexHullPoints.size();
        for (int i = 0; i < n; i++) {
            java.awt.Point p1 = convexHullPoints.get(i);
            java.awt.Point p2 = convexHullPoints.get((i + 1) % n);
            area += p1.x * p2.y - p1.y * p2.x;
        }
        area = Math.abs(area) / 2.0;

        return area;
    }

    private static BufferedImage processImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a new BufferedImage for the processed image
        BufferedImage processedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Iterate over each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the color of the current pixel
                Color color = new Color(image.getRGB(x, y));

                // Check if the pixel is part of the leaf
                if (isLeafPixel(color)) {
                    // Paint the leaf white
                    processedImage.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    // Paint the rest of the leaf black
                    processedImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return processedImage;
    }

    private static BufferedImage getLeafRegion(BufferedImage image) {
        // Find the bounding box of the leaf
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                if (rgb == Color.WHITE.getRGB()) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        // Extract the leaf region
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;
        BufferedImage leafRegion = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(minX + x, minY + y);
                leafRegion.setRGB(x, y, rgb);
            }
        }

        return leafRegion;
    }

    private static Ellipse2D.Double fitEllipse(BufferedImage image) {
        // Create a graphics context to draw on the image
        Graphics2D g2d = image.createGraphics();

        // Find the bounding box of the white region (leaf)
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Iterate through each pixel in the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                if (rgb == Color.WHITE.getRGB()) {
                    // Update bounding box coordinates
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        // Calculate the center and dimensions of the bounding box
        double centerX = (minX + maxX) / 2.0;
        double centerY = (minY + maxY) / 2.0;
        double width = maxX - minX + 1;
        double height = maxY - minY + 1;

        // Fit an ellipse to the bounding box
        Ellipse2D.Double ellipse = new Ellipse2D.Double(centerX - width / 2.0, centerY - height / 2.0, width, height);

        // Draw the ellipse (for visualization)
        g2d.setColor(Color.RED);
        g2d.draw(ellipse);

        // Dispose the graphics context
        g2d.dispose();

        return ellipse;
    }

    private static void saveImage(BufferedImage image, String filePath) {
        try {
            File output = new File(filePath);
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<java.awt.Point> findLeafPoints(BufferedImage image) {
        // Find the white pixels (leaf points)
        List<java.awt.Point> leafPoints = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                if (rgb == Color.WHITE.getRGB()) {
                    leafPoints.add(new Point(x, y));
                }
            }
        }

        return leafPoints;
    }

    private static double calculateFractalDimension(boolean[][] binaryImage) {
        int width = binaryImage[0].length;
        int height = binaryImage.length;

        // Calculate the maximum box size
        int maxBoxSize = Math.min(width, height) / 2;

        // Count the number of boxes covering the image at different scales
        int[] boxCount = new int[maxBoxSize + 1];
        for (int size = 1; size <= maxBoxSize; size++) {
            int count = 0;
            for (int y = 0; y < height - size; y += size) {
                for (int x = 0; x < width - size; x += size) {
                    boolean hasPoint = false;
                    // Check if the box contains any foreground pixels
                    outer:
                    for (int dy = 0; dy < size; dy++) {
                        for (int dx = 0; dx < size; dx++) {
                            if (binaryImage[y + dy][x + dx]) {
                                hasPoint = true;
                                break outer;
                            }
                        }
                    }
                    if (hasPoint) {
                        count++;
                    }
                }
            }
            boxCount[size] = count;
        }

        // Fit a linear regression line to the log-log plot of box count vs. box size
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;
        for (int size = 1; size <= maxBoxSize; size++) {
            double logSize = Math.log(size);
            double logCount = Math.log(boxCount[size]);
            sumX += logSize;
            sumY += logCount;
            sumXY += logSize * logCount;
            sumXX += logSize * logSize;
        }

        double n = maxBoxSize;
        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);

        // The slope of the linear regression line is the fractal dimension
        return -slope;
    }

    private static boolean isPerimeterPixel(BufferedImage image, int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Check if the pixel is a leaf pixel and has at least one background neighbor
        if (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
            if (new Color(image.getRGB(x, y)).equals(Color.WHITE)) {
                Color leftColor = new Color(image.getRGB(x - 1, y));
                Color rightColor = new Color(image.getRGB(x + 1, y));
                Color upColor = new Color(image.getRGB(x, y - 1));
                Color downColor = new Color(image.getRGB(x, y + 1));
                return leftColor.equals(Color.BLACK) || rightColor.equals(Color.BLACK) ||
                        upColor.equals(Color.BLACK) || downColor.equals(Color.BLACK);
            }
        }

        return false;
    }

    private static List<java.awt.Point> findConvexHull(List<java.awt.Point> points) {
        // Find the convex hull of the leaf points using Graham's scan algorithm
        List<java.awt.Point> convexHull = new ArrayList<>();

        // Find the point with the lowest y-coordinate (and the leftmost point if there are ties)
        java.awt.Point lowestPoint = points.get(0);
        for (java.awt.Point point : points) {
            if (point.y < lowestPoint.y || (point.y == lowestPoint.y && point.x < lowestPoint.x)) {
                lowestPoint = point;
            }
        }

        // Sort the points by polar angle with respect to the lowest point
        java.awt.Point finalLowestPoint = lowestPoint;
        points.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1.y - finalLowestPoint.y, p1.x - finalLowestPoint.x);
            double angle2 = Math.atan2(p2.y - finalLowestPoint.y, p2.x - finalLowestPoint.x);
            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            return Double.compare(p1.distanceSq(finalLowestPoint), p2.distanceSq(finalLowestPoint));
        });

        // Build the convex hull
        convexHull.add(points.get(0));
        for (int i = 1; i < points.size(); i++) {
            while (convexHull.size() >= 2 && orientation(convexHull.get(convexHull.size() - 2), convexHull.get(convexHull.size() - 1), points.get(i)) <= 0) {
                convexHull.remove(convexHull.size() - 1);
            }
            convexHull.add(points.get(i));
        }

        return convexHull;
    }

    private static int orientation(java.awt.Point p1, java.awt.Point p2, java.awt.Point p3) {
        // Compute the cross product of vectors (p2 - p1) and (p3 - p1)
        double crossProduct = (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
        if (crossProduct == 0) return 0; // Collinear
        return (crossProduct > 0) ? 1 : -1; // Clockwise or counterclockwise
    }

}
