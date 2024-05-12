package org.example.plantdisease.entity.templete;


import lombok.*;
import org.example.plantdisease.utils.ColumnKey;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public abstract class AbsUserAudit extends AbsDateAudit {

    //OBJECTNI KIM OCHGANI.
    // AGAR U SECURITYCONTEXTDA BO'LSA YOZILADI AKS HOLDA NULL
    @CreatedBy
    @Column(name = ColumnKey.CREATED_BY_ID, updatable = false)
    private UUID createdById;


    //OBJECTNI KIM O'ZGARTIRGANI.
    // AGAR U SECURITYCONTEXTDA BO'LSA YOZILADI AKS HOLDA NULL
    @LastModifiedBy
    @Column(name = ColumnKey.UPDATE_BY_ID)
    private UUID updatedById;

}
