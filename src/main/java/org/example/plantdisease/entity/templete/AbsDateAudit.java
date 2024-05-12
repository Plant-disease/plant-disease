package org.example.plantdisease.entity.templete;


import lombok.*;
import org.example.plantdisease.utils.ColumnKey;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * OBJECTLAR DB GA QO'SHILGANDA YOKI O'ZGARTIRILGANDA
 * AVTOMAT RAVISHDA O'SHA VAQTNI OLISHI UCHUN
 */
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbsDateAudit implements Serializable {
    @CreationTimestamp
    @Column(updatable = false, name = ColumnKey.CREATED_AT)
    private Timestamp createdAt;//OBJECT YANGI OCHIGANDA ISHLATILADI

    @UpdateTimestamp
    @Column(name = ColumnKey.UPDATED_AT)
    private Timestamp updatedAt;//OBJECT O'ZGARGANDA ISHLAYDI

    private Boolean deleted = false;

}
