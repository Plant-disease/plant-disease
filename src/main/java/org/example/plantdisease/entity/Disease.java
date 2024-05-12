package org.example.plantdisease.entity;


import lombok.*;
import org.example.plantdisease.entity.templete.AbsLongEntity;
import org.example.plantdisease.utils.TableNameConstant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

// BU CLASS ANIQLANADIGAN KASALLIK TURI (VTM, SPOT)

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = TableNameConstant.DISEASE)
@SQLDelete(sql = "UPDATE " + TableNameConstant.DISEASE + " SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Disease extends AbsLongEntity {

    private String name;   // KASALLIK NOMI (VTM, SPOT)
}
