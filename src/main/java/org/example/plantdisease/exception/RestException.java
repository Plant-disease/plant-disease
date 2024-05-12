package org.example.plantdisease.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.plantdisease.common.MessageService;
import org.example.plantdisease.payload.ErrorData;
import org.example.plantdisease.utils.ResponseMessage;
import org.example.plantdisease.utils.RestConstants;
import org.springframework.http.HttpStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {

    private String userMsg;
    private HttpStatus status;

    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private List<ErrorData> errors;
    private Integer errorCode;

    private RestException(String resourceName, String fieldName, Object fieldValue, String userMsg, HttpStatus status) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.userMsg = userMsg;
        this.status = status;
    }

    public RestException(String userMsg, HttpStatus status) {
        super(userMsg);
        this.userMsg = userMsg;
        this.status = status;
    }

    private RestException(HttpStatus status, List<ErrorData> errors) {
        this.status = status;
        this.errors = errors;
    }

    public static RestException restThrow(String message, HttpStatus httpStatus) {
        return new RestException(message, httpStatus);
    }

    public static RestException restThrow(String message) {
        return new RestException(message, HttpStatus.BAD_REQUEST);
    }

    public static RestException restThrow(String resourceName, String fieldName, Object fieldValue, String message, HttpStatus status) {
        return new RestException(resourceName, fieldName, fieldValue, message, status);
    }

    public static RestException restThrow(List<ErrorData> errors, HttpStatus status) {
        return new RestException(status, errors);
    }

    public static RestException restThrow(List<ErrorData> errors) {
        return new RestException(HttpStatus.BAD_REQUEST, errors);
    }


    private RestException(String resourceName, String fieldName, Object fieldValue, String userMsg) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.userMsg = userMsg;
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = RestConstants.NO_ITEMS_FOUND;
    }

    public static RestException notFound(String resourceKey) {
        return new RestException(
                ResponseMessage.notFound(resourceKey),
                HttpStatus.BAD_REQUEST
        );
    }

    public static RestException cannotDelete(String key) {
        return new RestException(
                ResponseMessage.cannotDelete(key),
                HttpStatus.BAD_REQUEST
        );
    }

    public static RestException notNull(String key) {
        String userMsg = ResponseMessage.notNull(key);
        return new RestException(userMsg, HttpStatus.BAD_REQUEST);
    }

    public static RestException typeRequired(String key) {
        String userMsg = ResponseMessage.typeRequired(key);
        return new RestException(userMsg, HttpStatus.BAD_REQUEST);
    }

    public static RestException typeNotValid(String key) {
        String userMsg = ResponseMessage.typeNotValid(key);
        return new RestException(userMsg, HttpStatus.BAD_REQUEST);
    }

    public static RestException alreadyExists(String resourceKey) {
        return new RestException(
                ResponseMessage.alreadyExists(resourceKey),
                HttpStatus.BAD_REQUEST
        );
    }

    public static RestException attackResponse() {
        return new RestException(
                ResponseMessage.ATTACK_RESPONSE,
                HttpStatus.BAD_REQUEST
        );
    }

    public static RestException forbidden() {
        return new RestException(
                ResponseMessage.FORBIDDEN,
                HttpStatus.FORBIDDEN
        );
    }

    public static RestException forbidden(String msg) {
        return new RestException(
                msg,
                HttpStatus.FORBIDDEN
        );
    }

    public static RestException required(String key) {
        String userMsg = ResponseMessage.required(key);
        return new RestException(
                userMsg,
                HttpStatus.BAD_REQUEST
        );
    }

//    public static RestException noAuthority() {
//        return new RestException(
//                MessageService.getMessage(ResponseMessage.UNAUTHORIZED),
//                HttpStatus.UNAUTHORIZED
//        );
//    }

    public static RestException noAuthority() {
        return new RestException(
                MessageService.getMessage(ResponseMessage.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

}
