package org.example.plantdisease.service;


import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.CheckDiseaseDTO;
import org.example.plantdisease.payload.CreateLeafDataDTO;
import org.springframework.http.ResponseEntity;


public interface LeafDataService {

    ApiResult<?> createLeafData(CreateLeafDataDTO createLeafDataDTO);

    ApiResult<?> checkDiseaseByKNN(CheckDiseaseDTO checkDiseaseDTO);

    ResponseEntity<?> downloadExcel();
}


