package org.example.plantdisease.service;


import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;

public interface DiseaseService {


    ApiResult<?> createDisease(String name);

    ApiResult<?> getAllDisease();

    ApiResult<?> updateDisease(LongNameWithValidDTO longNameWithValidDTO);

    ApiResult<?> deleteDisease(Long id);
}


