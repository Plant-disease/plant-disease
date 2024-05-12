package org.example.plantdisease.entity;


import lombok.*;
import org.example.plantdisease.entity.templete.AbsLongEntity;
import org.example.plantdisease.utils.TableNameConstant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

// BU CLASS BARGNING HOLATI (SOG'LOM, BIRINCHI DARAJALI KASALLANISH, IKKINCHI DARAJALI KASALLANISH)

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity(name = TableNameConstant.LEAF_CONDITION)
@SQLDelete(sql = "UPDATE " + TableNameConstant.LEAF_CONDITION + " SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class LeafCondition extends AbsLongEntity {

    private String name;   // BARGNING HOLATI (SOG'LOM, BIRINCHI DARAJALI KASALLANISH)

}
