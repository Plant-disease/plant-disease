package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.aop.CheckPermission;
import org.example.plantdisease.controller.LeafConditionController;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.service.LeafConditionService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeafConditionControllerImpl implements LeafConditionController {

    private final LeafConditionService leafConditionService;

    @CheckPermission(permissions = {Permission.ADD_LEAF_CONDITION})
    @Override
    public ApiResult<?> createLeafCondition(String name) {

        return leafConditionService.createLeafCondition(name);
    }

    @CheckPermission(permissions = {Permission.GET_ALL_LEAF_CONDITION})
    @Override
    public ApiResult<?> getAllLeafCondition() {

        return leafConditionService.getAllLeafCondition();
    }

    @CheckPermission(permissions = {Permission.EDIT_LEAF_CONDITION})
    @Override
    public ApiResult<?> updateLeafCondition(LongNameWithValidDTO longNameWithValidDTO) {

        return leafConditionService.updateLeafCondition(longNameWithValidDTO);
    }

    @CheckPermission(permissions = {Permission.DELETE_LEAF_CONDITION})
    @Override
    public ApiResult<?> deleteLeafCondition(Long id) {

        return leafConditionService.deleteLeafCondition(id);
    }


}

