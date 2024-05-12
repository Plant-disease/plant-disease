package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.Fruit;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.LongNameDTO;
import org.example.plantdisease.payload.LongNameWithValidDTO;
import org.example.plantdisease.repository.FruitRepository;
import org.example.plantdisease.service.FruitService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    @Override
    public ApiResult<?> createFruit(String name) {

        if (name.isEmpty())
            throw new RestException("NAME_SHOULD_NOT_BE_EMPTY", HttpStatus.BAD_REQUEST);

        Boolean exists = fruitRepository.existsByName(name);
        if (exists)
            throw new RestException("FRUIT_OF_THIS_NAME_HAS_ALREADY_BEEN_SAVED", HttpStatus.BAD_REQUEST);
        else{
            Fruit save = fruitRepository.save(new Fruit(name));

            return ApiResult.successResponse(save.getId(),"SAVED_SUCCESSFULLY");
        }
    }

    @Override
    public ApiResult<?> getAllFruit() {

        List<Fruit> fruitList = fruitRepository.findAll();

        return ApiResult.successResponse(toDTO(fruitList));
    }

    @Override
    public ApiResult<?> updateFruit(LongNameWithValidDTO longNameWithValidDTO) {

        Fruit fruit = fruitRepository.findById(longNameWithValidDTO.getId()).orElseThrow(() -> RestException.notFound("FRUIT_ID"));

        Boolean exists = fruitRepository.existsByNameAndIdNot(longNameWithValidDTO.getName(), longNameWithValidDTO.getId());
        if (exists)
            throw new RestException("FRUIT_OF_THIS_NAME_HAS_ALREADY_BEEN_SAVED", HttpStatus.BAD_REQUEST);
        else if (longNameWithValidDTO.getName().equals(fruit.getName()))
            throw new RestException("THIS_FRUIT_NAME_IS_THE_SAME_AS_FRUIT_NAME_TO_BE_CHANGED", HttpStatus.BAD_REQUEST);
        else {
            fruit.setName(longNameWithValidDTO.getName());
            fruitRepository.save(fruit);
        }

        return ApiResult.successResponse("UPDATED_SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> deleteFruit(Long id) {

        if (fruitRepository.existsById(id))
            fruitRepository.deleteById(id);
        else
            throw new RestException("FRUIT_ID_NOT_FOUND", HttpStatus.NOT_FOUND);


        return ApiResult.successResponse("DELETED_SUCCESSFULLY");
    }

    public LongNameDTO toDTO(Fruit fruit) {

        LongNameDTO longNameDTO = new LongNameDTO();
        longNameDTO.setId(fruit.getId());
        longNameDTO.setName(fruit.getName());

        return longNameDTO;
    }

    public List<LongNameDTO> toDTO(List<Fruit> fruitList) {

        List<LongNameDTO> longNameDTOList = new ArrayList<>();

        for (Fruit fruit : fruitList) {

            longNameDTOList.add(toDTO(fruit));
        }

        return longNameDTOList;
    }
}

