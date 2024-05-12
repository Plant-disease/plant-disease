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

import javax.persistence.*;
import java.util.List;

// BU CLASS DATASET YIG'ISH UCHUN RASM SAQLANAYOTGANDA ISHLATILADI

@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity(name = TableNameConstant.LEAF_DATA)
@SQLDelete(sql = "UPDATE " + TableNameConstant.LEAF_DATA + " SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class LeafData extends AbsLongEntity {

    @ManyToOne
    private Fruit fruit;

    @ManyToOne
    private Disease disease;

    @ManyToOne
    private LeafCondition leafCondition;

    @OneToMany()
    private List<Attachment> attachments;

    // LeafData SAQLANAYOTGANDA leafMeasurements NI SAQLAMAYMIZ BU BIZGA SHU LeafData NI GET QILAYOTGANIMIZDA FOYDALANAMIZ
    @OneToMany(mappedBy = "leafData",fetch = FetchType.EAGER)
    private List<LeafMeasurement> leafMeasurements;
}
