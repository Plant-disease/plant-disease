package org.example.plantdisease.entity;


import lombok.*;
import org.example.plantdisease.entity.templete.AbsUUIDUserAuditEntity;
import org.example.plantdisease.utils.TableNameConstant;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import java.util.Objects;


@Getter
@AllArgsConstructor
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = TableNameConstant.ATTACHMENT)
public class Attachment extends AbsUUIDUserAuditEntity {

    private String fileOriginalName;//pdp.jpg

    private long size;//1024000

    private String contentType;//image/png
    private String url; //url

//    private String fileNameInSystem; //This is file nameUz in System

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Attachment that = (Attachment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Attachment(String fileOriginalName, long size, String contentType) {
        this.fileOriginalName = fileOriginalName;
        this.size = size;
        this.contentType = contentType;
    }
}

