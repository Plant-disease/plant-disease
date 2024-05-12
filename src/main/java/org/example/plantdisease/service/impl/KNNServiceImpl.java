package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.LeafData;
import org.example.plantdisease.entity.LeafMeasurement;
import org.example.plantdisease.payload.LeafDataDTO;
import org.example.plantdisease.repository.LeafDataRepository;
import org.example.plantdisease.service.KNNService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Service
@RequiredArgsConstructor
public class KNNServiceImpl implements KNNService {

    private final LeafDataRepository leafDataRepository;


    // BU METOD KNN ALGORITMI ORQALI KIRITILGAN BARGNI DATASET DAGI BARGLAR BILAN SOLISHTIRADI
    // VA KIRITILGAN RASMLARNI HAR BIRI QAYSI KASALLIKGA YAQIN EKANLIGINI ANIQLAB SHU KASALLIK
    // DiseaseId LARINI QAYTARADI
    @Override
    public List<Long> checkDiseaseByKNN(List<LeafMeasurement> leafMeasurementList, Long fruitId, Long diseaseId) {

        List<Long> numberList = new ArrayList<>();

        // BAZADAN SHU POMIDOR TURIGA VA KASALLIK TURIGA TEGISHLI BO'LGAN LeafData LARNI OLAMIZ
        List<LeafData> leafDataList = leafDataRepository.findAllByFruitIdAndDiseaseId(fruitId, diseaseId);

        for (LeafMeasurement leafMeasurement : leafMeasurementList) {

            // LeafMeasurement QIYMATLARINI MASSIVE GA O'GIRAMIZ
            List<Double> inputMeasurement = convertToMassive(leafMeasurement);

            numberList.add(checkLeafMeasurementByKNN(leafDataList, inputMeasurement));
        }

        numberList.forEach(System.out::println);
        return numberList;
    }


    // BU METOD BITTA BARGNI O'LCHAMINI DATASETDAGI BARGLAR O'LCHAMLARI BILAN KNN ORQALI TEKSHIRADI
    // VA QAYSI KASALLIKGA TEGISHLI EKANLIGINI(LEAF_CONDITION_ID ) QAYTARADI
    public Long checkLeafMeasurementByKNN(List<LeafData> leafDataList, List<Double> inputMeasurement) {

        List<LeafDataDTO> leafDataDTOList = new ArrayList<>();

        for (LeafData leafData : leafDataList) {

            // BAZADAN OLINGAN LeafData NING LeafMeasurement LARINI MASSIVE GA O'GIRAMIZ
            List<List<Double>> measurements = convertToMassive(leafData.getLeafMeasurements());

            // KIRITILGAN BARGNING O'LCHAMI BILAN BAZADAN OLINGAN O'LCHAMNI ORASIDAGI MASOFANI HISOBLAYMIZ
            double sum = calculateDistant(inputMeasurement, measurements);

            // QAYSI KASALLIKGA TEGISHLI EKANINI ANIQLASH UCHUN LeafDataDTO CLASS GA YIG'INDI VA KASALLIK TURINI SAQLAB QO'YAMIZ
            leafDataDTOList.add(new LeafDataDTO(sum, leafData.getLeafCondition().getId()));
        }

        return findLargestNumber(leafDataDTOList);
    }


    // BU METOD KIRITILGAN BARG O'LCHAMLATRI BILAN BAZADAN OLINGAN BARG O'LCHAMLARI ORASIDAGI MASOFANI ANIQLAYDI
    public double calculateDistant(List<Double> inputMeasurement, List<List<Double>> measurements) {

        double sum = 0;
        for (List<Double> measurement : measurements) {

            double x = measurement.get(0) - inputMeasurement.get(0);
            double x1 = measurement.get(1) - inputMeasurement.get(1);
            double x2 = measurement.get(2) - inputMeasurement.get(2);
            double x3 = measurement.get(3) - inputMeasurement.get(3);
            double x4 = measurement.get(4) - inputMeasurement.get(4);
            double x5 = measurement.get(5) - inputMeasurement.get(5);
            double x6 = measurement.get(6) - inputMeasurement.get(6);
            double x7 = measurement.get(7) - inputMeasurement.get(7);
            double x8 = measurement.get(8) - inputMeasurement.get(8);
            double x9 = measurement.get(9) - inputMeasurement.get(9);
            double x10 = measurement.get(10) - inputMeasurement.get(10);
//            double x11 = measurement.get(11) - inputMeasurement.get(11);

            sum += sqrt((pow(x, 2) + pow(x1, 2) + pow(x2, 2) + pow(x3, 2) + pow(x4, 2) + pow(x5, 2) +
                    pow(x6, 2) + pow(x7, 2) + pow(x8, 2) + pow(x9, 2) + pow(x10, 2)));
        }

        return sum;
    }


    // BU METOD YIG'INDILAR ICHIDAN ENG KATTASINI VA SHU KASALLIK TURINI ANIQLAYDI
    public Long findLargestNumber(List<LeafDataDTO> leafDataDTOList) {


        for (int i = 0; i < leafDataDTOList.size(); i++) {

            System.out.println((i + 1) + "-yig'indi = " + leafDataDTOList.get(i));
        }

        double smallestNumber = 0, smallestNumberIndex = 0, repeatSmallestNumber = 0, repeatSmallestNumberIndex = 0;
        Long leafConditionId = 0L;

        for (int i = 0; i < leafDataDTOList.size(); i++) {

            double sum = leafDataDTOList.get(i).getSum();
            if (i == 0) {
                smallestNumber = sum;
                leafConditionId = leafDataDTOList.get(i).getLeafConditionId();
            } else if (smallestNumber > sum) {
                smallestNumber = sum;
                smallestNumberIndex = i;
                leafConditionId = leafDataDTOList.get(i).getLeafConditionId();
            } else if (smallestNumber == sum) {
                repeatSmallestNumber = sum;
                repeatSmallestNumberIndex = i;
            }

        }

        if (smallestNumber == repeatSmallestNumber && smallestNumberIndex != repeatSmallestNumberIndex)
            return 0L;

        return leafConditionId;
    }


    // BU METOD LeafMeasurement QIYMATLARINI MASSIVE GA O'GIRADI
    public List<Double> convertToMassive(LeafMeasurement leafMeasurement) {

        List<Double> columns = new ArrayList<>();
        columns.add(leafMeasurement.getLeafArea());
        columns.add(leafMeasurement.getElongation());
        columns.add(leafMeasurement.getCompactness());
        columns.add(leafMeasurement.getCircularity());
        columns.add(leafMeasurement.getLeafPerimeter());
        columns.add(leafMeasurement.getTextureEnergy());
        columns.add(leafMeasurement.getTotalVeinCount());
        columns.add(leafMeasurement.getLeafAspectRatio());
        columns.add(leafMeasurement.getFractalDimension());
        columns.add(leafMeasurement.getAverageVeinWidth());
        columns.add(leafMeasurement.getAverageVeinLength());

        return columns;
    }


    // BU METOD LeafMeasurement QIYMATLARINI MASSIVE GA O'GIRADI
    public List<List<Double>> convertToMassive(List<LeafMeasurement> leafMeasurementList) {

        List<List<Double>> rows = new ArrayList<>();

        for (LeafMeasurement leafMeasurement : leafMeasurementList) {

            rows.add(convertToMassive(leafMeasurement));
        }

        return rows;
    }

}
