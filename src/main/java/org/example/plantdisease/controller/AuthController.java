package org.example.plantdisease.controller;


import org.example.plantdisease.payload.*;
import org.example.plantdisease.utils.RestConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RequestMapping(AuthController.AUTH_CONTROLLER_PATH)
public interface AuthController {
    String AUTH_CONTROLLER_PATH = RestConstants.BASE_PATH_V1 + "/auth";
    String CHECK_PHONE_PATH = "/check-phone";
    String CHECK_EMAIL_PATH = "/check-email";
    String VERIFY_PHONE_PATH = "/verify-phone";
    String VERIFY_EMAIL_PATH = "/verify-email";
    String CHANGE_PASSWORD = "/change-password";
    String ADMIN_CABINET = "/personal-cabinet/admin";
    String LOGIN = "/login";
    String CHECK_EMAIL_TO_CHANGE_PASSWORD = "/change-password/check-email";
    String VERIFY_EMAIL_TO_CHANGE_PASSWORD = "/change-password/verify-email";

    @PostMapping(CHECK_PHONE_PATH)
    ApiResult<?> checkPhoneNumber(@Valid @RequestBody TempUserDTO tempUserDTO);

    @PostMapping(VERIFY_PHONE_PATH)
    ApiResult<?> checkVerifyPhoneNumber(@Valid @RequestBody VerifyPhoneDTO verifyPhoneDTO);

    @PostMapping(CHECK_EMAIL_PATH)
    ApiResult<?> checkEmail(@Valid @RequestBody RegisterDTO registerDTO);

    @PostMapping(VERIFY_EMAIL_PATH)
    ApiResult<?> checkVerifyEmail(@Valid @RequestBody VerifyEmailDTO verifyEmailDTO);

//    @PostMapping(ADMIN_CABINET)
//    ApiResult<?> adminCabinet(@Valid @RequestBody LoginDTO loginDTO);

    @PostMapping(LOGIN)
    ApiResult<?> login(@Valid @RequestBody LoginDTO loginDTO);

    @PostMapping(CHANGE_PASSWORD)
    ApiResult<?> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO);

    @PostMapping(CHECK_EMAIL_TO_CHANGE_PASSWORD)
    ApiResult<?> checkEmailToForgotPassword(@Valid @RequestParam @NotBlank(message = "{EMAIL_SHOULD_NOT_BE_EMPTY}") @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                                                    flags = Pattern.Flag.CASE_INSENSITIVE, message = "{EMAIL_PATTERN}") String email);

    @PostMapping(VERIFY_EMAIL_TO_CHANGE_PASSWORD)
    ApiResult<?> checkVerifyEmailToForgotPassword(@Valid @RequestBody VerifyEmailDTO verifyEmailDTO);

}
