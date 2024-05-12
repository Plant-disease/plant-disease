package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.*;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.CheckDiseaseDTO;
import org.example.plantdisease.payload.CreateLeafDataDTO;
import org.example.plantdisease.payload.GetLeafDataDTO;
import org.example.plantdisease.repository.*;
import org.example.plantdisease.service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;


@Service
@RequiredArgsConstructor
public class LeafDataServiceImpl implements LeafDataService {

    private final KNNService knnService;
    private final MainService mainService;
    private final ExcelService excelService;
    private final FruitRepository fruitRepository;
    private final DiseaseRepository diseaseRepository;
    private final LeafDataRepository leafDataRepository;
    private final AttachmentRepository attachmentRepository;
    private final LeafMeasurementService leafMeasurementService;
    private final LeafConditionRepository leafConditionRepository;
    private final LeafMeasurementRepository leafMeasurementRepository;

    @Override
    public ApiResult<?> createLeafData(CreateLeafDataDTO createLeafDataDTO) {

        Fruit fruit = fruitRepository.findById(createLeafDataDTO.getFruitId()).orElseThrow(() -> RestException.notFound("FRUIT_ID"));
        Disease disease = diseaseRepository.findById(createLeafDataDTO.getDiseaseId()).orElseThrow(() -> RestException.notFound("DISEASE_ID"));
        LeafCondition leafCondition = leafConditionRepository.findById(createLeafDataDTO.getLeafConditionId()).orElseThrow(() -> RestException.notFound("LEAF_CONDITION_ID"));

        if (mainService.checkDuplicateNumber(createLeafDataDTO.getAttachments()))
            throw new RestException("DUPLICATE ATTACHMENT_IDS", HttpStatus.NOT_FOUND);

        List<Attachment> attachmentList = attachmentRepository.findAllByIdIn(createLeafDataDTO.getAttachments());
        if (attachmentList.isEmpty() || attachmentList.size() != createLeafDataDTO.getAttachments().size())
            throw new RestException("ATTACHMENT NOT FOUND OR ENTERED ATTACHMENT_IDS NOT IN DATABASE", HttpStatus.NOT_FOUND);

        LeafData leafData = saveLeafDataToDB(fruit, disease, attachmentList, leafCondition);

        List<LeafMeasurement> leafMeasurementList = leafMeasurementService.getLeafMeasurementToSaveDB(leafData, attachmentList);
        leafMeasurementRepository.saveAll(leafMeasurementList);

        return ApiResult.successResponse("SAVED_SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> checkDiseaseByKNN(CheckDiseaseDTO checkDiseaseDTO) {

        if (mainService.checkDuplicateNumber(checkDiseaseDTO.getAttachmentIds()))
            throw new RestException("DUPLICATE ATTACHMENT_IDS", HttpStatus.NOT_FOUND);

        List<Attachment> attachmentList = attachmentRepository.findAllByIdIn(checkDiseaseDTO.getAttachmentIds());
        if (attachmentList.isEmpty() || attachmentList.size() != checkDiseaseDTO.getAttachmentIds().size())
            throw new RestException("ATTACHMENT NOT FOUND OR ENTERED ATTACHMENT_IDS NOT IN DATABASE", HttpStatus.NOT_FOUND);

        List<LeafMeasurement> leafMeasurementList = leafMeasurementService.getLeafMeasurementToCheckKNN(attachmentList);
        List<Long> numberList = knnService.checkDiseaseByKNN(leafMeasurementList, checkDiseaseDTO.getFruitId(), checkDiseaseDTO.getDiseaseId());
        String result = mainService.getInstructionByDiseaseId(numberList);

        return ApiResult.successResponse(result);
    }

    @Override
    public ResponseEntity<?> downloadExcel() {

        try {
            List<LeafData> leafDataList = leafDataRepository.findAll();

            List<Map<Integer, String>> data = excelService.makeExcelDataForLeafMeasurement(toDTO(leafDataList));
            ByteArrayOutputStream byteArrayOutputStream = excelService.generateExcel(ExcelService.COLUMN_1, data, "LEAF_MEASUREMENT");

            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "ProductTemplate" + ".xlsx");

            return new ResponseEntity<>(new ByteArrayResource(byteArrayOutputStream.toByteArray()), header, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<GetLeafDataDTO> toDTO(List<LeafData> leafDataList) {

        List<GetLeafDataDTO> getLeafDataDTOList = new ArrayList<>();

        for (LeafData leafData : leafDataList) {

            for (LeafMeasurement leafMeasurement : leafData.getLeafMeasurements()) {

                GetLeafDataDTO getLeafDataDTO = new GetLeafDataDTO();
                getLeafDataDTO.setId(leafData.getId());
                getLeafDataDTO.setFruit(leafData.getFruit().getName());
                getLeafDataDTO.setDisease(leafData.getDisease().getName());
                getLeafDataDTO.setLeafCondition(leafData.getLeafCondition().getName());

                getLeafDataDTO.setLeafArea(leafMeasurement.getLeafArea());
                getLeafDataDTO.setElongation(leafMeasurement.getElongation());
                getLeafDataDTO.setCompactness(leafMeasurement.getCompactness());
                getLeafDataDTO.setCircularity(leafMeasurement.getCircularity());
                getLeafDataDTO.setLeafPerimeter(leafMeasurement.getLeafPerimeter());
                getLeafDataDTO.setTextureEnergy(leafMeasurement.getTextureEnergy());
                getLeafDataDTO.setTotalVeinCount(leafMeasurement.getTotalVeinCount());
                getLeafDataDTO.setLeafAspectRatio(leafMeasurement.getLeafAspectRatio());
                getLeafDataDTO.setAverageVeinWidth(leafMeasurement.getAverageVeinWidth());
                getLeafDataDTO.setFractalDimension(leafMeasurement.getFractalDimension());
                getLeafDataDTO.setAverageVeinLength(leafMeasurement.getAverageVeinLength());

                getLeafDataDTOList.add(getLeafDataDTO);
            }
        }
        return getLeafDataDTOList;
    }

    public LeafData saveLeafDataToDB(Fruit fruit, Disease disease, List<Attachment> attachmentList, LeafCondition leafCondition) {

        Optional<LeafData> leafDataOptional = leafDataRepository.findByFruitIdAndDiseaseIdAndLeafConditionId(fruit.getId(), disease.getId(), leafCondition.getId());

        LeafData leafData;
        if (leafDataOptional.isEmpty()) {

            leafData = new LeafData();
            leafData.setFruit(fruit);
            leafData.setDisease(disease);
            leafData.setAttachments(attachmentList);
            leafData.setLeafCondition(leafCondition);
        } else {

            leafData = leafDataOptional.get();
            List<Attachment> attachments = leafData.getAttachments();
            attachments.addAll(attachmentList);
            leafData.setAttachments(attachments);
        }

        try {

            return leafDataRepository.save(leafData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException("ENTERED_IMAGES_INCLUDE_THOSE_PREVIOUSLY_ATTACHED_TO_LEAF_DATA", HttpStatus.BAD_REQUEST);
        }
    }

}

