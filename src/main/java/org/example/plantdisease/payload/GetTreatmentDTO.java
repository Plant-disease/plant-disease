package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class GetTreatmentDTO {

    private Long id;
    private String instruction;
    private Long leafConditionId;
    private String leafConditionName;
}
