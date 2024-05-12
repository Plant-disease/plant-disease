package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class UpdateTreatmentDTO {

    private Long id;
    private Long leafConditionId;
    @NotBlank(message = "{INSTRUCTION_SHOULD_NOT_BE_EMPTY}")
    private String instruction;
}
