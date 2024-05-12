package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class LongNameWithValidDTO {

    private Long id;
    @NotBlank(message = "{NAME_SHOULD_NOT_BE_EMPTY}")
    private String name;
}
