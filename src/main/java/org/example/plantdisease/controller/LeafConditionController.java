package org.example.plantdisease.controller;



import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.utils.RestConstants;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping(LeafConditionController.LEAF_CONDITION_CONTROLLER)
public interface LeafConditionController {

    String LEAF_CONDITION_CONTROLLER = RestConstants.BASE_PATH_V1 + "/leaf-condition";
    String CREATE_LEAF_CONDITION = "/create";
    String GET_ALL_LEAF_CONDITION = "/get-all";
    String UPDATE_LEAF_CONDITION = "/update";
    String DELETE_LEAF_CONDITION = "/delete";


    @PostMapping(CREATE_LEAF_CONDITION)
    ApiResult<?> createLeafCondition(@RequestParam String name);

    @GetMapping(GET_ALL_LEAF_CONDITION)
    ApiResult<?> getAllLeafCondition();

    @PutMapping(UPDATE_LEAF_CONDITION)
    ApiResult<?> updateLeafCondition(@Valid @RequestBody LongNameWithValidDTO longNameWithValidDTO);

    @DeleteMapping(DELETE_LEAF_CONDITION)
    ApiResult<?> deleteLeafCondition(@RequestParam Long id);
}

