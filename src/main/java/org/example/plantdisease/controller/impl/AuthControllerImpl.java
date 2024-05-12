package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.controller.AuthController;
import org.example.plantdisease.payload.*;
import org.example.plantdisease.service.AuthService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Override
    public ApiResult<?> checkPhoneNumber(TempUserDTO tempUserDTO) {
        return authService.checkPhoneNumber(tempUserDTO);
    }

    @Override
    public ApiResult<?> checkVerifyPhoneNumber(VerifyPhoneDTO verifyPhoneDTO) {
        return authService.checkVerifyPhoneNumber(verifyPhoneDTO);
    }

    @Override
    public ApiResult<?> checkEmail(RegisterDTO registerDTO) {
        return authService.checkEmail(registerDTO);
    }

    @Override
    public ApiResult<?> checkVerifyEmail(VerifyEmailDTO verifyEmailDTO) {
        return authService.checkVerifyEmail(verifyEmailDTO);
    }


//    @Override
//    public ApiResult<?> adminCabinet(LoginDTO loginDTO) {
//        return authService.adminCabinet(loginDTO);
//    }

    @Override
    public ApiResult<?> login(LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @Override
    public ApiResult<?> forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        return authService.forgotPassword(forgotPasswordDTO);
    }

    @Override
    public ApiResult<?> checkEmailToForgotPassword(String email) {
        return authService.checkEmailToForgotPassword(email);
    }

    @Override
    public ApiResult<?> checkVerifyEmailToForgotPassword(VerifyEmailDTO verifyEmailDTO) {
        return authService.checkVerifyEmailToForgotPassword(verifyEmailDTO);
    }

}
