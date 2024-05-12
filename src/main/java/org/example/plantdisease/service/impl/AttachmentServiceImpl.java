package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
import org.example.plantdisease.entity.Attachment;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.repository.AttachmentContentRepository;
import org.example.plantdisease.repository.AttachmentRepository;
import org.example.plantdisease.service.AttachmentService;
import org.example.plantdisease.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    @Value("${attachment.package.url}")
    String packageUrl;

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    @Override
    public ApiResult<?> uploadFileToFileSystem(MultipartHttpServletRequest request) {

        if (!CommonUtils.getSecretKeyForAttachment()) {
            throw RestException.restThrow("GO AWAY", HttpStatus.BAD_REQUEST);
        }

        Iterator<String> fileNames = request.getFileNames();

        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {

            Attachment attachment = attachmentRepository.save(saveAttachment(file));

            return ApiResult.successResponse(attachment.getId());
        }

        return ApiResult.errorResponse("ERROR!!", 404);
    }

    @Override
    public ApiResult<?> uploadAllFileToFileSystem(MultipartHttpServletRequest request) {

        if (!CommonUtils.getSecretKeyForAttachment()) {
            throw RestException.restThrow("GO AWAY", HttpStatus.BAD_REQUEST);
        }

        List<Attachment> attachmentList = new ArrayList<>();
        Iterator<String> fileNames = request.getFileNames();

        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            MultipartFile file = request.getFile(fileName);

            if (file == null)
                return ApiResult.errorResponse("ERROR!!", 404);

            attachmentList.add(saveAttachment(file));

        }
        List<Attachment> savedAttachmentList = attachmentRepository.saveAll(attachmentList);

        return ApiResult.successResponse(getIdFromAttachment(savedAttachmentList));
    }

    //    @SneakyThrows
//    @Override
//    public ApiResult<?> uploadFileToDB(MultipartHttpServletRequest request) {
//
//        if (!CommonUtils.getSecretKeyForAttachment()) {
//            throw RestException.restThrow("GO AWAY", HttpStatus.BAD_REQUEST);
//        }
//
//        Iterator<String> fileNames = request.getFileNames();
//        MultipartFile file = request.getFile(fileNames.next());
//        if (file != null) {
//
//            //FILE HAQIDA MA'LUMOT OLISH
//
//            String originalFilename = file.getOriginalFilename();
//            long size = file.getSize();
//            String contentType = file.getContentType();
//
//            Attachment attachment = new Attachment(originalFilename, size, contentType);
//            attachmentRepository.save(attachment);
//
//            //FILE NI CONTENT(byte[]) SAQLAYMIZ
//
//            AttachmentContent attachmentContent = new AttachmentContent();
//            try {
//                attachmentContent.setContent(file.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            attachmentContent.setAttachment(attachment);
//            attachmentContentRepository.save(attachmentContent);
//
//
//            return ApiResult.successResponse(attachment.getId());
//
//        }
//
//        return ApiResult.errorResponse("ERROR!!", 404);
//    }

    //    @SneakyThrows
    @Override
    public ApiResult<?> downloadFileFromSystem(UUID id, HttpServletResponse response) {

        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);

        if (optionalAttachment.isPresent()) {

            Attachment attachment = optionalAttachment.get();

            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + attachment.getFileOriginalName());

            response.setContentType(attachment.getContentType());

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(attachment.getUrl());
                FileCopyUtils.copy(fileInputStream, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ApiResult.errorResponse("FILE_NOT_FOUND", 400);
    }

    //    @SneakyThrows
//    @Override
//    public ApiResult<?> downloadFileFromDB(UUID id, HttpServletResponse response) {
//        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> RestException.restThrow("FILE NOT FOUND", HttpStatus.NOT_FOUND));
//        Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());
//        if (optionalAttachmentContent.isPresent()) {
//            AttachmentContent attachmentContent = optionalAttachmentContent.get();
//
//            response
//                    .setHeader("Content-Disposition",
//                            "attachment; filename=\"" + attachment.getContentType() + "\"");
//
//            response
//                    .setContentType(attachment.getContentType());
//            try {
//                FileCopyUtils.copy(attachmentContent.getContent(), response.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return ApiResult.successResponse(MessageService.getMessage("Successfully saved"));
//    }
//
//    @Override
//    public ApiResult<?> uploadAvatarToDB(MultipartHttpServletRequest request) {
//
//        Iterator<String> fileNames = request.getFileNames();
//        MultipartFile file = request.getFile(fileNames.next());
//        if (file != null) {
//
//            Attachment attachment = attachmentRepository.save(saveAttachment(file));
//
//            return ApiResult.successResponse(attachment);
//        }
//        return ApiResult.errorResponse("ERROR!!", 404);
//    }

    //    @SneakyThrows
    private Attachment saveAttachment(MultipartFile file) {

        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        Attachment attachment = new Attachment();
        attachment.setFileOriginalName(originalFilename);
        attachment.setContentType(file.getContentType());
        attachment.setSize(file.getSize());

        String uuid = UUID.randomUUID().toString();
        String[] split = originalFilename.split("\\.");
        String fileName = uuid + "." + split[split.length - 1];

        Path path = Paths.get(packageUrl + "/" + fileName);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        attachment.setUrl(path.toString());
        return attachment;
    }

    private List<UUID> getIdFromAttachment(List<Attachment> attachmentList) {

        List<UUID> uuidList = new ArrayList<>();

        for (Attachment attachment : attachmentList) {

            uuidList.add(attachment.getId());
        }
        return uuidList;
    }

}

