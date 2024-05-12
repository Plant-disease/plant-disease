package org.example.plantdisease.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.plantdisease.entity.templete.AbsUUIDUserAuditEntity;
import org.example.plantdisease.utils.TableNameConstant;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = TableNameConstant.ATTACHMENT_CONTENT)
public class AttachmentContent extends AbsUUIDUserAuditEntity {

    private byte[] content;//asosiy content

    @OneToOne
    private Attachment attachment;

}

