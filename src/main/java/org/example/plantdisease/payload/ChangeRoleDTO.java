package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class ChangeRoleDTO {

//    @NotBlank(message = "{USER_ID_SHOULD_NOT_BE_EMPTY}")
    private UUID userId;
//    @NotBlank(message = "{ROLE_ID_SHOULD_NOT_BE_EMPTY}")
    private Long roleId;
}
