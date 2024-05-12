package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.aop.CheckPermission;
import org.example.plantdisease.controller.TreatmentController;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.CreateTreatmentDTO;
import org.example.plantdisease.payload.UpdateTreatmentDTO;
import org.example.plantdisease.service.TreatmentService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TreatmentControllerImpl implements TreatmentController {

    private final TreatmentService treatmentService;

    @CheckPermission(permissions = {Permission.ADD_TREATMENT})
    @Override
    public ApiResult<?> createTreatment(CreateTreatmentDTO createTreatmentDTO) {

        return treatmentService.createTreatment(createTreatmentDTO);
    }

    @CheckPermission(permissions = {Permission.GET_ALL_TREATMENT})
    @Override
    public ApiResult<?> getAllTreatment() {

        return treatmentService.getAllTreatment();
    }

    @CheckPermission(permissions = {Permission.EDIT_TREATMENT})
    @Override
    public ApiResult<?> updateTreatment(UpdateTreatmentDTO updateTreatmentDTO) {

        return treatmentService.updateTreatment(updateTreatmentDTO);
    }

    @CheckPermission(permissions = {Permission.DELETE_TREATMENT})
    @Override
    public ApiResult<?> deleteTreatment(Long id) {

        return treatmentService.deleteTreatment(id);
    }


}

