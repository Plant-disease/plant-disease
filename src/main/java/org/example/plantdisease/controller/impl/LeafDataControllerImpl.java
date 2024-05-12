package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.aop.CheckPermission;
import org.example.plantdisease.controller.LeafDataController;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.CheckDiseaseDTO;
import org.example.plantdisease.payload.CreateLeafDataDTO;
import org.example.plantdisease.service.LeafDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LeafDataControllerImpl implements LeafDataController {

    private final LeafDataService leafDataService;

    @CheckPermission(permissions = {Permission.ADD_LEAF_DATA})
    @Override
    public ApiResult<?> createLeafData(CreateLeafDataDTO createLeafDataDTO) {
        return leafDataService.createLeafData(createLeafDataDTO);
    }

    @CheckPermission(permissions = {Permission.CHECK_LEAF_DISEASE_BY_KNN})
    @Override
    public ApiResult<?> checkDiseaseByKNN(CheckDiseaseDTO checkDiseaseDTO) {
        return leafDataService.checkDiseaseByKNN(checkDiseaseDTO);
    }

    @CheckPermission(permissions = {Permission.DOWNLOAD_EXCEL})
    @Override
    public ResponseEntity<?> downloadExcel() {
        return leafDataService.downloadExcel();
    }
}

