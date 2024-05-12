package org.example.plantdisease.service;


import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;

public interface FruitService {


    ApiResult<?> createFruit(String name);

    ApiResult<?> getAllFruit();

    ApiResult<?> updateFruit(LongNameWithValidDTO longNameWithValidDTO);

    ApiResult<?> deleteFruit(Long id);
}


