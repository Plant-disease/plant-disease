package org.example.plantdisease.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.plantdisease.entity.templete.AbsUUIDNotUserAuditEntity;
import org.example.plantdisease.utils.ColumnKey;

import javax.persistence.Column;
import javax.persistence.Entity;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class VerifyCode extends AbsUUIDNotUserAuditEntity {

    @Column(nullable = false,name = ColumnKey.CODE)
    private String code;

    @Column(name = ColumnKey.PHONE_NUMBER)
    private String phoneNumber;

    @Column(nullable = false,name = ColumnKey.EMAIL)
    private String email;

    @Column(name = ColumnKey.FULL_NAME)
    private String fullName;

    @Column(name = ColumnKey.PASSWORD)
    private String password;

    @Column(name = ColumnKey.CONFIRMED)
    private boolean confirmed;

    public VerifyCode(String code, String phoneNumber, String fullName,String password) {
        this.code = code;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
    }
    public VerifyCode(String code, String phoneNumber, String fullName) {
        this.code = code;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

}
