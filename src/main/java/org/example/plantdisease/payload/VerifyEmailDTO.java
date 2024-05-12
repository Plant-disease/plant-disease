package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyEmailDTO {
    @NotBlank(message = "{VERIFY_CODE_SHOULD_NOT_BE_EMPTY}")
    private String verifyCode;
    @NotBlank(message = "{EMAIL_SHOULD_NOT_BE_EMPTY}")
    private String email;
}
