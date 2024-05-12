package org.example.plantdisease.controller;



import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.utils.RestConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestMapping(AttachmentController.ATTACHMENT_CONTROLLER)
public interface AttachmentController {

    String ATTACHMENT_CONTROLLER = RestConstants.BASE_PATH_V1 + "/attachment";

//    String UPLOAD_FILE_TO_DB = "/upload";
    String UPLOAD_FILE_TO_FILE_SYSTEM = "/system/upload";
    String UPLOAD_ALL_FILE_TO_FILE_SYSTEM = "/system/upload/all";
//    String DOWNLOAD_FILE_FROM_DB = "/download/{id}";
    String DOWNLOAD_FILE_FROM_FILE_SYSTEM = "/system/download/{id}";

    @PostMapping(UPLOAD_FILE_TO_FILE_SYSTEM)
    ApiResult<?> uploadFileToFileSystem(MultipartHttpServletRequest request);

    @PostMapping(UPLOAD_ALL_FILE_TO_FILE_SYSTEM)
    ApiResult<?> uploadAllFileToFileSystem(MultipartHttpServletRequest request);

//    @PostMapping(UPLOAD_FILE_TO_DB)
//    ApiResult<?> uploadFileToDB(MultipartHttpServletRequest request);
//
//    @GetMapping(DOWNLOAD_FILE_FROM_DB)
//    ApiResult<?> downloadFileFromDB(@PathVariable UUID id, HttpServletResponse response);

    @GetMapping(DOWNLOAD_FILE_FROM_FILE_SYSTEM)
    ApiResult<?> downloadFileFromSystem(@PathVariable UUID id, HttpServletResponse response);

}

