package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class CreateTreatmentDTO {

    private Long leafConditionId;
    @NotBlank(message = "{INSTRUCTION_SHOULD_NOT_BE_EMPTY}")
    private String instruction;
}
