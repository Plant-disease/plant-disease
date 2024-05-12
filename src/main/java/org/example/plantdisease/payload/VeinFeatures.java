package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VeinFeatures {

    private double meanIntensity;
    private double variance;
    private double totalVeinCount;
    private double averageVeinLength;
    private double averageVeinWidth;

    public VeinFeatures(double totalVeinCount, double averageVeinLength, double averageVeinWidth) {
        this.totalVeinCount = totalVeinCount;
        this.averageVeinLength = averageVeinLength;
        this.averageVeinWidth = averageVeinWidth;
    }
}
