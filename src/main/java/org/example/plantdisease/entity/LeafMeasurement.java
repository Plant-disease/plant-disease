package org.example.plantdisease.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.plantdisease.entity.templete.AbsLongEntity;
import org.example.plantdisease.utils.TableNameConstant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

// BU CLASS KNN ALGORITMI UCHUN BITTA BARGNING O'LCHAMLARI SAQLASH UCHUN

@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity(name = TableNameConstant.LEAF_MEASUREMENT)
@SQLDelete(sql = "UPDATE " + TableNameConstant.LEAF_MEASUREMENT + " SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class LeafMeasurement extends AbsLongEntity {

    @ManyToOne
    private LeafData leafData;

    private Double leafArea;
    private Double elongation;
    private Double compactness;
    private Double circularity;
    private Double textureEnergy;
    private Double leafPerimeter;
    private double totalVeinCount;
    private Double leafAspectRatio;
    private Double fractalDimension;
    private double averageVeinWidth;
    private double averageVeinLength;

//    private Double entropy;
//    private Double variance;
//    private Double leafLength;
//    private Double meanIntensity;

}
