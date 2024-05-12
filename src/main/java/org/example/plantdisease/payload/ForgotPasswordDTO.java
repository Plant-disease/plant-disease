package org.example.plantdisease.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.plantdisease.utils.MessageConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDTO {

    @NotBlank(message = "{EMAIL_SHOULD_NOT_BE_EMPTY}")
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "{EMAIL_PATTERN}")
    private String email;
    @NotBlank(message = MessageConstants.VERIFY_CODE_SHOULD_NOT_BE_EMPTY)
    private String verifyCode;
    @NotBlank(message = MessageConstants.PASSWORD_SHOULD_NOT_BE_EMPTY)
    private String password;
    @NotBlank(message = MessageConstants.PRE_PASSWORD_SHOULD_NOT_BE_EMPTY)
    private String prePassword;
}
