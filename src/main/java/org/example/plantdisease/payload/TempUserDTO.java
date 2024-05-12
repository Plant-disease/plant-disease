package org.example.plantdisease.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.plantdisease.utils.RestConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TempUserDTO {
    @NotBlank(message = "{FULL_NAME_SHOULD_NOT_BE_EMPTY}")
    private String fullName;
    @NotBlank(message = "{PHONE_NUMBER_SHOULD_NOT_BE_EMPTY}")
    @Pattern(regexp = RestConstants.PHONE_NUMBER_REGEX, message = "{PHONE_NUMBER_PATTERN}")
    private String phoneNumber;
}
