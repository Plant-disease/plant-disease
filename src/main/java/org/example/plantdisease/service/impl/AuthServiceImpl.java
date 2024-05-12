package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.plantdisease.common.MessageService;
import org.example.plantdisease.entity.User;
import org.example.plantdisease.entity.VerifyCode;
import org.example.plantdisease.enums.RoleType;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.*;
import org.example.plantdisease.repository.RoleRepository;
import org.example.plantdisease.repository.UserRepository;
import org.example.plantdisease.repository.VerifyCodeRepository;
import org.example.plantdisease.security.JwtProvider;
import org.example.plantdisease.service.AuthService;
import org.example.plantdisease.service.EmailSenderService;
import org.example.plantdisease.service.MainService;
import org.example.plantdisease.utils.CommonUtils;
import org.example.plantdisease.utils.MessageConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final VerifyCodeRepository verifyCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;
    private final MainService mainService;

    @Value("${app.max-sms-count}")
    private long maxSmsCount;

    @Value("${app.max-sms-waiting-time}")
    private long maxSmsWaitingTime;

    @Override
    public ApiResult<?> checkPhoneNumber(TempUserDTO tempUserDTO) {

        Timestamp pastTime = new Timestamp(System.currentTimeMillis() - 43_200_000);
        long count = verifyCodeRepository.countAllByPhoneNumberAndCreatedAtAfter(tempUserDTO.getPhoneNumber(), pastTime);
        if (count == maxSmsCount) {
            throw RestException.restThrow("TO MANY REQUEST", HttpStatus.TOO_MANY_REQUESTS);
        }

        String code = CommonUtils.getRandomNumberString();
//        playMobileSMSService.sendSMS(tempUserDTO.getPhoneNumber(), code);
        System.out.println(tempUserDTO + ">>" + code);
        verifyCodeRepository.save(new VerifyCode(code, tempUserDTO.getPhoneNumber(), tempUserDTO.getFullName()));
        return ApiResult.successResponse(code, MessageService.getMessage("VERIFICATION_CODE_SEND_YOUR_PHONE_NUMBER"));

    }

    @Override
    public ApiResult<?> checkVerifyPhoneNumber(VerifyPhoneDTO verifyPhoneDTO) {

        Timestamp pastTime = new Timestamp(System.currentTimeMillis() - maxSmsWaitingTime);
        VerifyCode verifyCode = verifyCodeRepository.findFirstByPhoneNumberAndConfirmedIsFalseAndCreatedAtAfterOrderByCreatedAtDesc
                (verifyPhoneDTO.getPhoneNumber(), pastTime).orElseThrow(() -> RestException.restThrow("PLEASE TRY AGAIN!!", HttpStatus.CONFLICT));

        if (!verifyCode.getCode().equals(verifyPhoneDTO.getVerifyCode()))
            throw RestException.restThrow("WRONG VERIFICATION CODE", HttpStatus.CONFLICT);

        String idNumber;
        verifyCode.setConfirmed(true);
        verifyCodeRepository.save(verifyCode);
        Optional<User> optionalUser = userRepository.findByPhoneNumber(verifyCode.getPhoneNumber());
        if (optionalUser.isEmpty()) {

            idNumber = generateUniqueIdNumber(verifyPhoneDTO.getVerifyCode());
            User user = new User();
            user.setPhoneNumber(verifyCode.getPhoneNumber());
            user.setUsername(verifyCode.getPhoneNumber());
            user.setFullName(verifyCode.getFullName());
            user.setPassword(passwordEncoder.encode(idNumber));
            user.setRole(roleRepository.findByType(RoleType.GUEST).orElseThrow(() -> RestException.restThrow("ROLE_NOT_FOUND", HttpStatus.NOT_FOUND)));
            userRepository.save(user);
        } else idNumber = optionalUser.get().getEmail();

        return ApiResult.successResponse(idNumber, MessageService.getMessage("VERIFICATION SUCCESSFULLY"));

    }


    @SneakyThrows
    @Override
    public ApiResult<?> checkEmail(RegisterDTO registerDTO) {

        Timestamp pastTime = new Timestamp(System.currentTimeMillis() - 43_200_000);
        long count = verifyCodeRepository.countAllByEmailAndCreatedAtAfter(registerDTO.getEmail(), pastTime);
        if (count == maxSmsCount) {
            throw RestException.restThrow("TO MANY REQUEST", HttpStatus.TOO_MANY_REQUESTS);
        }

        Optional<User> optionalUser = userRepository.findByEmail(registerDTO.getEmail());
        if (optionalUser.isPresent())
            throw RestException.restThrow("YOU HAVE ALREADY REGISTERED!!", HttpStatus.CONFLICT);

        mainService.checkPasswordAndPrePassword(registerDTO.getPassword(), registerDTO.getPrePassword());

        String code = CommonUtils.getRandomNumberString();

        emailSenderService.sendCodeToEmail(registerDTO.getEmail(), code);

        System.out.println(registerDTO + ">>" + code);

        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setPassword(registerDTO.getPassword());
        verifyCode.setEmail(registerDTO.getEmail());
        verifyCode.setCode(code);
        verifyCode.setFullName(registerDTO.getFullName());
        verifyCodeRepository.save(verifyCode);
        return ApiResult.successResponse(code, MessageService.getMessage("VERIFICATION_CODE_SEND_YOUR_EMAIL"));

    }

    @Override
    public ApiResult<?> checkVerifyEmail(VerifyEmailDTO verifyEmailDTO) {

        Timestamp pastTime = new Timestamp(System.currentTimeMillis() - maxSmsWaitingTime);
        VerifyCode verifyCode = verifyCodeRepository.findFirstByEmailAndConfirmedIsFalseAndCreatedAtAfterOrderByCreatedAtDesc
                (verifyEmailDTO.getEmail(), pastTime).orElseThrow(() -> RestException.restThrow("PLEASE TRY AGAIN!!", HttpStatus.CONFLICT));

        if (!verifyCode.getCode().equals(verifyEmailDTO.getVerifyCode()))
            throw RestException.restThrow("WRONG VERIFICATION CODE", HttpStatus.CONFLICT);

        verifyCode.setConfirmed(true);
        verifyCodeRepository.save(verifyCode);
        Optional<User> optionalUser = userRepository.findByEmail(verifyCode.getEmail());
        if (optionalUser.isEmpty()) {

            User user = new User();
            user.setEmail(verifyCode.getEmail());
            user.setUsername(verifyCode.getEmail());
            user.setFullName(verifyCode.getFullName());
            user.setPassword(passwordEncoder.encode(verifyCode.getPassword()));
            user.setRole(roleRepository.findByType(RoleType.GUEST).orElseThrow(() -> RestException.restThrow("ROLE_NOT_FOUND", HttpStatus.NOT_FOUND)));
            userRepository.save(user);
        }

        return ApiResult.successResponse(MessageService.getMessage("VERIFICATION SUCCESSFULLY"));
    }

    @Override
    public ApiResult<?> login(LoginDTO loginDTO) {
        try {
            User user = checkUserUserNameAndPasswordAndEtcAndSetAuthenticationOrThrow(loginDTO.getEmail(), loginDTO.getPassword());
            String accessToken = jwtProvider.generateToken(user.getUsername(), true);
            String refreshToken = jwtProvider.generateToken(user.getUsername(), false);

            if (user.isSuperAdmin())
                return ApiResult.successResponse(new TokenDTO(accessToken, true, refreshToken));
            else if (user.getRole().getType().equals(RoleType.ADMIN))
                return ApiResult.successResponse(new TokenDTO(accessToken, refreshToken, true));
            else
                return ApiResult.successResponse(new TokenDTO(accessToken, refreshToken));

        } catch (Exception e) {
            e.printStackTrace();
            throw RestException.restThrow("WRONG_EMAIL_OR_PASSWORD", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ApiResult<?> forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {

        verifyCodeRepository.findFirstByEmailAndCodeAndConfirmedIsTrueOrderByCreatedAtDesc(forgotPasswordDTO.getEmail(), forgotPasswordDTO.getVerifyCode()).
                orElseThrow(RestException::forbidden);

        mainService.checkPasswordAndPrePassword(forgotPasswordDTO.getPassword(), forgotPasswordDTO.getPrePassword());

        User user = userRepository.findByEmail(forgotPasswordDTO.getEmail()).orElseThrow(() -> RestException.notFound("USER"));

        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getPassword()));

        userRepository.save(user);

        return ApiResult.successResponse(MessageConstants.PASSWORD_CHANGED_SUCCESSFULLY);
    }

    @SneakyThrows
    @Override
    public ApiResult<?> checkEmailToForgotPassword(String email) {

        Timestamp pastTime = new Timestamp(System.currentTimeMillis() - 43_200_000);
        long count = verifyCodeRepository.countAllByEmailAndCreatedAtAfter(email, pastTime);
        if (count == maxSmsCount) {
            throw RestException.restThrow("TO MANY REQUEST", HttpStatus.TOO_MANY_REQUESTS);
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty())
            throw RestException.restThrow("YOU DON'T HAVE  REGISTERED!!", HttpStatus.CONFLICT);

        String code = CommonUtils.getRandomNumberString();

        emailSenderService.sendCodeToEmail(email, code);

        System.out.println(email + ">>" + code);

        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setEmail(email);
        verifyCode.setCode(code);
        verifyCodeRepository.save(verifyCode);

        return ApiResult.successResponse(code, MessageService.getMessage("VERIFICATION_CODE_SEND_YOUR_EMAIL"));
    }

    @Override
    public ApiResult<?> checkVerifyEmailToForgotPassword(VerifyEmailDTO verifyEmailDTO) {

        Timestamp pastTime = new Timestamp(System.currentTimeMillis() - maxSmsWaitingTime);
        VerifyCode verifyCode = verifyCodeRepository.findFirstByEmailAndConfirmedIsFalseAndCreatedAtAfterOrderByCreatedAtDesc
                (verifyEmailDTO.getEmail(), pastTime).orElseThrow(() -> RestException.restThrow("PLEASE TRY AGAIN!!", HttpStatus.CONFLICT));

        if (!verifyCode.getCode().equals(verifyEmailDTO.getVerifyCode()))
            throw RestException.restThrow("WRONG VERIFICATION CODE", HttpStatus.CONFLICT);

        verifyCode.setConfirmed(true);
        verifyCodeRepository.save(verifyCode);

        return ApiResult.successResponse(MessageService.getMessage("VERIFICATION SUCCESSFULLY"));
    }


//    @Override
//    public ApiResult<?> adminCabinet(LoginDTO loginDTO) {
//        try {
//            User user = checkUserIdNumberAndPasswordAndEtcAndSetAuthenticationOrThrow(loginDTO.getUserName(), loginDTO.getPassword());
//            String accessToken = jwtProvider.generateToken(user.getUsername(), true);
//            String refreshToken = jwtProvider.generateToken(user.getUsername(), false);
//            return ApiResult.successResponse(new TokenDTO(accessToken, refreshToken));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw RestException.restThrow("WRONG_PHONE_NUMBER_OR_PASSWORD", HttpStatus.UNAUTHORIZED);
//        }
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        USERNI PHONENUMBER ORQALI DB DAN OLYAPDI TOPILMASA THROW TASHLAYDI
        return userRepository
                .findFirstByUsernameAndEnabledIsTrueAndAccountNonExpiredIsTrueAndCredentialsNonExpiredIsTrueAndAccountNonLockedIsTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
//        Optional<User> optionalUser = userRepository.findFirstByUsernameAndEnabledIsTrueAndAccountNonExpiredIsTrueAndCredentialsNonExpiredIsTrueAndAccountNonLockedIsTrue(username);
//        if (optionalUser.isEmpty()) {
//            return userRepository.findByEmail(username).orElseThrow(() -> RestException.restThrow("WRONG_USERNAME_OR_PASSWORD", HttpStatus.UNAUTHORIZED));
//        } else if (Objects.equals(optionalUser.get().getRole().getType(), RoleType.ADMIN)) {
//            return optionalUser.get();
//        } else
//            throw RestException.restThrow("WRONG_USERNAME_OR_PASSWORD", HttpStatus.UNAUTHORIZED);

    }

    private User checkUserIdNumberAndPasswordAndEtcAndSetAuthenticationOrThrow(String phoneNumber, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return (User) authentication.getPrincipal();
        } catch (DisabledException | LockedException | CredentialsExpiredException disabledException) {
            throw RestException.restThrow("USER_NOT_FOUND_OR_DISABLED", HttpStatus.FORBIDDEN);
        } catch (BadCredentialsException | UsernameNotFoundException badCredentialsException) {
            throw RestException.restThrow("LOGIN_OR_PASSWORD_ERROR", HttpStatus.FORBIDDEN);
        }
    }

    private User checkUserUserNameAndPasswordAndEtcAndSetAuthenticationOrThrow(String phoneNumber, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return (User) authentication.getPrincipal();
        } catch (DisabledException | LockedException | CredentialsExpiredException disabledException) {
            throw RestException.restThrow("USER_NOT_FOUND_OR_DISABLED", HttpStatus.FORBIDDEN);
        } catch (BadCredentialsException | UsernameNotFoundException badCredentialsException) {
            throw RestException.restThrow("LOGIN_OR_PASSWORD_ERROR", HttpStatus.FORBIDDEN);
        }
    }


    private User checkUserForRegister(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (!Objects.equals(user.getRole().getType(), RoleType.GUEST)) {
                throw RestException.restThrow("YOU HAVE ALREADY REGISTERED!!", HttpStatus.CONFLICT);
            }
        }
        return user;
    }

    /**
     * ID GENERATE FOR USER
     **/

    private String generateUniqueIdNumber(String idNumber) {

        // DATABASE DAN SHU ID NUMBER BORLIGINI TEKSHIRAMIZ

        Boolean exists = true;

        if (userRepository.existsByIdNumber(idNumber)) {

            while (exists) {
                idNumber = CommonUtils.getRandomNumberString();
                exists = userRepository.existsByIdNumber(idNumber);
            }
        }

        return idNumber;
    }


}
