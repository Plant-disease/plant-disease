package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyPhoneDTO {
    @NotBlank(message = "{VERIFY_CODE_SHOULD_NOT_BE_EMPTY}")
    private String verifyCode;
    @NotBlank(message = "{PHONE_NUMBER_SHOULD_NOT_BE_EMPTY}")
    private String phoneNumber;
}
