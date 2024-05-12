package org.example.plantdisease.utils;

import java.util.Objects;

public interface ResponseMessage {
    String SERVER_ERROR = "SERVER_ERROR";
    String ALREADY_EXISTS = "ALREADY_EXISTS";
    String NOT_FOUND = "NOT_FOUND";
    String SUCCESS_SAVE = "SUCCESS_SAVE";
    String CANNOT_SAVE = "CANNOT_SAVE";
    String SUCCESS_DELETE = "SUCCESS_DELETE";
    String CANNOT_DELETE = "CANNOT_DELETE";
    String SUCCESS_EDIT = "SUCCESS_EDIT";
    String CANNOT_EDIT = "CANNOT_EDIT";
    String REQUIRED = "REQUIRED";
    String TYPE_NOT_VALID = "TYPE_NOT_VALID";
    String NOT_NULL = "NOT_NULL";
    String ATTACK_RESPONSE = "ATTACK_RESPONSE";
    String FORBIDDEN = "FORBIDDEN";

    String UNAUTHORIZED = "UN_AUTHORIZED";


    static String alreadyExists(String key) {
        if (Objects.isNull(key)) {
            return ALREADY_EXISTS;
        }
        return key.toUpperCase() + "_" + ALREADY_EXISTS;
    }

    static String notFound(String key) {
        if (Objects.isNull(key)) {
            return NOT_FOUND;
        }
        return key.toUpperCase() + "_" + NOT_FOUND;
    }

    static String successSave(String key) {
        if (Objects.isNull(key)) {
            return SUCCESS_SAVE;
        }
        return key.toUpperCase() + "_" + SUCCESS_SAVE;
    }

    static String cannotSave(String key) {
        if (Objects.isNull(key)) {
            return CANNOT_SAVE;
        }
        return key.toUpperCase() + "_" + CANNOT_SAVE;
    }

    static String successDelete(String key) {
        if (Objects.isNull(key)) {
            return SUCCESS_DELETE;
        }
        return key.toUpperCase() + "_" + SUCCESS_DELETE;
    }

    static String cannotDelete(String key) {
        if (Objects.isNull(key)) {
            return CANNOT_DELETE;
        }
        return key.toUpperCase() + "_" + CANNOT_DELETE;
    }

    static String successEdit(String key) {
        if (Objects.isNull(key)) {
            return SUCCESS_EDIT;
        }
        return key.toUpperCase() + "_" + SUCCESS_EDIT;
    }

    static String cannotEdit(String key) {
        if (Objects.isNull(key)) {
            return CANNOT_EDIT;
        }
        return key.toUpperCase() + "_" + CANNOT_EDIT;
    }

    static String notNull(String key) {
        if (Objects.isNull(key)) {
            return NOT_NULL;
        }
        return key.toUpperCase() + "_" + NOT_NULL;
    }

    static String typeRequired(String key) {
        if (Objects.isNull(key)) {
            return REQUIRED;
        }
        return key.toUpperCase() + "_" + REQUIRED;
    }

    static String typeNotValid(String key) {
        if (Objects.isNull(key)) {
            return TYPE_NOT_VALID;
        }
        return key.toUpperCase() + "_" + TYPE_NOT_VALID;
    }

    static String required(String key) {
        if (Objects.isNull(key)) {
            return REQUIRED;
        }
        return key.toUpperCase() + "_" + REQUIRED;
    }
}
