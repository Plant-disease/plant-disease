package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.aop.CheckPermission;
import org.example.plantdisease.controller.FruitController;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.service.FruitService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FruitControllerImpl implements FruitController {

    private final FruitService fruitService;

    @CheckPermission(permissions = {Permission.ADD_FRUIT})
    @Override
    public ApiResult<?> createFruit(String name) {

        return fruitService.createFruit(name);
    }

    @CheckPermission(permissions = {Permission.GET_ALL_FRUIT})
    @Override
    public ApiResult<?> getAllFruit() {

        return fruitService.getAllFruit();
    }

    @CheckPermission(permissions = {Permission.EDIT_FRUIT})
    @Override
    public ApiResult<?> updateFruit(LongNameWithValidDTO longNameWithValidDTO) {

        return fruitService.updateFruit(longNameWithValidDTO);
    }

    @CheckPermission(permissions = {Permission.DELETE_FRUIT})
    @Override
    public ApiResult<?> deleteFruit(Long id) {

        return fruitService.deleteFruit(id);
    }


}

