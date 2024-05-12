package org.example.plantdisease.service;


import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.CreateTreatmentDTO;
import org.example.plantdisease.payload.UpdateTreatmentDTO;

public interface TreatmentService {


    ApiResult<?> createTreatment(CreateTreatmentDTO createTreatmentDTO);

    ApiResult<?> getAllTreatment();

    ApiResult<?> updateTreatment(UpdateTreatmentDTO updateTreatmentDTO);

    ApiResult<?> deleteTreatment(Long id);
}


