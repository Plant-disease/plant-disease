package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.Disease;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameDTO;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.repository.DiseaseRepository;
import org.example.plantdisease.service.DiseaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Override
    public ApiResult<?> createDisease(String name) {

        if (name.isEmpty())
            throw new RestException("NAME_SHOULD_NOT_BE_EMPTY", HttpStatus.BAD_REQUEST);

        Boolean exists = diseaseRepository.existsByName(name);
        if (exists)
            throw new RestException("DISEASE_OF_THIS_NAME_HAS_ALREADY_BEEN_SAVED", HttpStatus.BAD_REQUEST);
        else{

            Disease save = diseaseRepository.save(new Disease(name));
            return ApiResult.successResponse(save.getId(),"SAVED_SUCCESSFULLY");
        }

    }

    @Override
    public ApiResult<?> getAllDisease() {

        List<Disease> diseaseList = diseaseRepository.findAll();

        return ApiResult.successResponse(toDTO(diseaseList));
    }

    @Override
    public ApiResult<?> updateDisease(LongNameWithValidDTO longNameWithValidDTO) {

        Disease disease = diseaseRepository.findById(longNameWithValidDTO.getId()).orElseThrow(() -> RestException.notFound("DISEASE_ID"));

        Boolean exists = diseaseRepository.existsByNameAndIdNot(longNameWithValidDTO.getName(), longNameWithValidDTO.getId());
        if (exists)
            throw new RestException("DISEASE_OF_THIS_NAME_HAS_ALREADY_BEEN_SAVED", HttpStatus.BAD_REQUEST);
        else if (longNameWithValidDTO.getName().equals(disease.getName()))
            throw new RestException("THIS_DISEASE_NAME_IS_THE_SAME_AS_DISEASE_NAME_TO_BE_CHANGED", HttpStatus.BAD_REQUEST);
        else {
            disease.setName(longNameWithValidDTO.getName());
            diseaseRepository.save(disease);
        }

        return ApiResult.successResponse("UPDATED_SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> deleteDisease(Long id) {

        if (diseaseRepository.existsById(id))
            diseaseRepository.deleteById(id);
        else
            throw new RestException("DISEASE_ID_NOT_FOUND", HttpStatus.NOT_FOUND);


        return ApiResult.successResponse("DELETED_SUCCESSFULLY");
    }

    public LongNameDTO toDTO(Disease disease) {

        LongNameDTO longNameDTO = new LongNameDTO();
        longNameDTO.setId(disease.getId());
        longNameDTO.setName(disease.getName());

        return longNameDTO;
    }

    public List<LongNameDTO> toDTO(List<Disease> diseaseList) {

        List<LongNameDTO> longNameDTOList = new ArrayList<>();

        for (Disease disease : diseaseList) {

            longNameDTOList.add(toDTO(disease));
        }

        return longNameDTOList;
    }
}

