package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.aop.CheckPermission;
import org.example.plantdisease.controller.DiseaseController;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.service.DiseaseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiseaseControllerImpl implements DiseaseController {

    private final DiseaseService diseaseService;


    @CheckPermission(permissions = {Permission.ADD_DISEASE})
    @Override
    public ApiResult<?> createDisease(String name) {
        return diseaseService.createDisease(name);
    }

    @CheckPermission(permissions = {Permission.GET_ALL_DISEASE})
    @Override
    public ApiResult<?> getAllDisease() {
        return diseaseService.getAllDisease();
    }

    @CheckPermission(permissions = {Permission.EDIT_DISEASE})
    @Override
    public ApiResult<?> updateDisease(LongNameWithValidDTO longNameWithValidDTO) {
        return diseaseService.updateDisease(longNameWithValidDTO);
    }

    @CheckPermission(permissions = {Permission.DELETE_DISEASE})
    @Override
    public ApiResult<?> deleteDisease(Long id) {
        return diseaseService.deleteDisease(id);
    }
}

