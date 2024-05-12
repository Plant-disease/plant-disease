package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLeafDataDTO {

    private Long id ;
    private String fruit ;
    private String disease ;
    private String leafCondition ;

    private Double entropy ;
    private Double variance;
    private Double leafArea ;
    private Double leafLength ;
    private Double elongation ;
    private Double compactness ;
    private Double circularity ;
    private Double meanIntensity;
    private Double textureEnergy;
    private Double leafPerimeter ;
    private Double leafAspectRatio ;
    private Double fractalDimension ;

    private double totalVeinCount;
    private double averageVeinWidth;
    private double averageVeinLength;
}
