package org.example.plantdisease.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageProcessing {

    @Value("${attachment.second.package.url}")
    String packageUrl;

    public String getWhiteImage(String path) {
        // Load the image
        BufferedImage image = loadImage(path);

        // Process the image
        BufferedImage processedImage = processImage(image);

        String imagePath = packageUrl + "\\output_image.jpg";

        // Save the processed image
        saveImage(processedImage, imagePath);

        return imagePath;
    }

    private static BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveImage(BufferedImage image, String filePath) {
        try {
            File output = new File(filePath);
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static boolean isLeafPixel(Color color) {
        // Threshold values for detecting green color of the leaf
        int greenThreshold = 100;

        // Check if the color is close to green (leaf color)
        return color.getRed() < greenThreshold && color.getGreen() > greenThreshold && color.getBlue() < greenThreshold;
    }
}
