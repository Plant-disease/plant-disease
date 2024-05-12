package org.example.plantdisease.service;


import org.example.plantdisease.payload.ApiResult;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public interface AttachmentService {

    ApiResult<?> uploadFileToFileSystem(MultipartHttpServletRequest request);

    ApiResult<?> uploadAllFileToFileSystem(MultipartHttpServletRequest request);

//    ApiResult<?> uploadFileToDB(MultipartHttpServletRequest request);

    ApiResult<?> downloadFileFromSystem(UUID id, HttpServletResponse response);

//    ApiResult<?> downloadFileFromDB(UUID id, HttpServletResponse response);

//    ApiResult<?> uploadAvatarToDB(MultipartHttpServletRequest request) throws IOException;
}


