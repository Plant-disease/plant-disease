package org.example.plantdisease.service;



import org.example.plantdisease.entity.LeafMeasurement;

import java.util.List;

public interface KNNService {

    List<Long> checkDiseaseByKNN(List<LeafMeasurement> leafMeasurementList, Long fruitId, Long diseaseId);

}