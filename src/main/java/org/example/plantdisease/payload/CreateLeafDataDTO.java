package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class CreateLeafDataDTO {

    private Long fruitId;
    private Long diseaseId;
    private Long leafConditionId;

    private List<UUID> attachments;

}
