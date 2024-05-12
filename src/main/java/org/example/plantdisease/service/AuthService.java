package org.example.plantdisease.service;


import org.example.plantdisease.payload.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {
    ApiResult<?> checkPhoneNumber(TempUserDTO tempUserDTO);

    ApiResult<?> checkVerifyPhoneNumber(VerifyPhoneDTO verifyPhoneDTO);


//    ApiResult<?> adminCabinet(LoginDTO loginDTO);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    ApiResult<?> checkEmail(RegisterDTO registerDTO);

    ApiResult<?> checkVerifyEmail(VerifyEmailDTO verifyEmailDTO);

    ApiResult<?> login(LoginDTO loginDTO);

    ApiResult<?> forgotPassword(ForgotPasswordDTO forgotPasswordDTO);

    ApiResult<?> checkEmailToForgotPassword(String email);

    ApiResult<?> checkVerifyEmailToForgotPassword(VerifyEmailDTO verifyEmailDTO);
}
