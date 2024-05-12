package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "{USER_NAME_SHOULD_NOT_BE_EMPTY}")
    private String email;
    @NotBlank(message = "PASSWORD_SHOULD_NOT_BE_EMPTY")
    private String password;

}
