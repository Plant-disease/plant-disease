package org.example.plantdisease.controller;


import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.utils.RestConstants;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping(DiseaseController.DISEASE_CONTROLLER)
public interface DiseaseController {

    String DISEASE_CONTROLLER = RestConstants.BASE_PATH_V1 + "/disease";
    String CREATE_DISEASE = "/create";
    String GET_ALL_DISEASE = "/get-all";
    String UPDATE_DISEASE = "/update";
    String DELETE_DISEASE = "/delete";


    @PostMapping(CREATE_DISEASE)
    ApiResult<?> createDisease(@RequestParam String name);

    @GetMapping(GET_ALL_DISEASE)
    ApiResult<?> getAllDisease();

    @PutMapping(UPDATE_DISEASE)
    ApiResult<?> updateDisease(@Valid @RequestBody LongNameWithValidDTO longNameWithValidDTO);

    @DeleteMapping(DELETE_DISEASE)
    ApiResult<?> deleteDisease(@RequestParam Long id);
}

