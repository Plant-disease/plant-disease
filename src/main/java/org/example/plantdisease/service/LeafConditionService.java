package org.example.plantdisease.service;


import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;

public interface LeafConditionService {


    ApiResult<?> createLeafCondition(String name);

    ApiResult<?> getAllLeafCondition();

    ApiResult<?> updateLeafCondition(LongNameWithValidDTO longNameWithValidDTO);

    ApiResult<?> deleteLeafCondition(Long id);
}


