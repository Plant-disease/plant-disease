package org.example.plantdisease.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.plantdisease.entity.templete.AbsLongEntity;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.enums.RoleType;
import org.example.plantdisease.utils.ColumnKey;
import org.example.plantdisease.utils.TableNameConstant;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = TableNameConstant.ROLE)
public class Role extends AbsLongEntity {

    @Column(nullable = false, unique = true, name = ColumnKey.NAME)
    private String name;

    @Column(name = ColumnKey.DESCRIPTION,length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    public Role(String name, RoleType type, Set<Permission> permissions) {
        this.name = name;
        this.type = type;
        this.permissions = permissions;
    }
}
