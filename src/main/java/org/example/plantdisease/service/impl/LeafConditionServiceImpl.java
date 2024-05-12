package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.LeafCondition;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameDTO;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.repository.LeafConditionRepository;
import org.example.plantdisease.service.LeafConditionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LeafConditionServiceImpl implements LeafConditionService {

    private final LeafConditionRepository leafConditionRepository;

    @Override
    public ApiResult<?> createLeafCondition(String name) {

        if (name.isEmpty())
            throw new RestException("NAME_SHOULD_NOT_BE_EMPTY", HttpStatus.BAD_REQUEST);

        Boolean exists = leafConditionRepository.existsByName(name);
        if (exists)
            throw new RestException("LEAF_CONDITION_OF_THIS_NAME_HAS_ALREADY_BEEN_SAVED", HttpStatus.BAD_REQUEST);
        else{

            LeafCondition save = leafConditionRepository.save(new LeafCondition(name));
            return ApiResult.successResponse(save.getId(),"SAVED_SUCCESSFULLY");
        }
    }

    @Override
    public ApiResult<?> getAllLeafCondition() {

        List<LeafCondition> leafConditionList = leafConditionRepository.findAll();

        return ApiResult.successResponse(toDTO(leafConditionList));
    }

    @Override
    public ApiResult<?> updateLeafCondition(LongNameWithValidDTO longNameWithValidDTO) {

        LeafCondition leafCondition = leafConditionRepository.findById(longNameWithValidDTO.getId()).orElseThrow(() -> RestException.notFound("LEAF_CONDITION_ID"));

        Boolean exists = leafConditionRepository.existsByNameAndIdNot(longNameWithValidDTO.getName(), longNameWithValidDTO.getId());
        if (exists)
            throw new RestException("LEAF_CONDITION_OF_THIS_NAME_HAS_ALREADY_BEEN_SAVED", HttpStatus.BAD_REQUEST);
        else if (longNameWithValidDTO.getName().equals(leafCondition.getName()))
            throw new RestException("THIS_LEAF_CONDITION_NAME_IS_THE_SAME_AS_LEAF_CONDITION_NAME_TO_BE_CHANGED", HttpStatus.BAD_REQUEST);
        else {
            leafCondition.setName(longNameWithValidDTO.getName());
            leafConditionRepository.save(leafCondition);
        }

        return ApiResult.successResponse("UPDATED_SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> deleteLeafCondition(Long id) {

        if (leafConditionRepository.existsById(id))
            leafConditionRepository.deleteById(id);
        else
            throw new RestException("LEAF_CONDITION_ID_NOT_FOUND", HttpStatus.NOT_FOUND);


        return ApiResult.successResponse("DELETED_SUCCESSFULLY");
    }

    public LongNameDTO toDTO(LeafCondition leafCondition) {

        LongNameDTO longNameDTO = new LongNameDTO();
        longNameDTO.setId(leafCondition.getId());
        longNameDTO.setName(leafCondition.getName());

        return longNameDTO;
    }

    public List<LongNameDTO> toDTO(List<LeafCondition> leafConditionList) {

        List<LongNameDTO> longNameDTOList = new ArrayList<>();

        for (LeafCondition leafCondition : leafConditionList) {

            longNameDTOList.add(toDTO(leafCondition));
        }

        return longNameDTOList;
    }
}

