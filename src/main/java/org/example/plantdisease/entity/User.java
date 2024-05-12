package org.example.plantdisease.entity;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.plantdisease.entity.templete.AbsUUIDUserAuditEntity;
import org.example.plantdisease.utils.ColumnKey;
import org.example.plantdisease.utils.TableNameConstant;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@javax.persistence.Entity(name = TableNameConstant.USERS)
@SQLDelete(sql = "UPDATE " + TableNameConstant.USERS + " SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class User extends AbsUUIDUserAuditEntity implements UserDetails {

    //ISMI
    @Column(name = ColumnKey.FULL_NAME)
    private String fullName;

    //TELEFON RAQAMI
    @Column(name = ColumnKey.PHONE_NUMBER, unique = true)
    private String phoneNumber;

    //LOGIN UCHUN TELEFON RAQAM YOKI EMAIL BIRIKTIRILADI
    @Column(nullable = false, name = ColumnKey.USER_NAME, unique = true)
    private String username;

    //TIZIMGA KIRUVCHI PAROLI
    @Column(name = ColumnKey.PASSWORD)
    private String password;

    @Column(name = ColumnKey.EMAIL)
    private String email;

    @JoinColumn(name = ColumnKey.ATTACHMENT_ID)
    @OneToOne
    private Attachment photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ColumnKey.ROLE_ID, nullable = false)
    @ToString.Exclude
    private Role role;

    @Column(name = ColumnKey.ACCOUNT_NON_EXPIRED)
    private boolean accountNonExpired = true;

    @Column(name = ColumnKey.ACCOUNT_NON_LOCKED)
    private boolean accountNonLocked = true;

    @Column(name = ColumnKey.CREDENTIALS_NON_EXPIRED)
    private boolean credentialsNonExpired = true;

    @Column(name = ColumnKey.ENABLED)
    private boolean enabled=true;

//    @Column(name = ColumnKey.IS_ADMIN)
//    private boolean isAdmin = false;

    @Column(name = ColumnKey.IS_SUPER_ADMIN)
    private boolean isSuperAdmin = false;

    public User(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public User(String fullName, String email,String username, String password, Role role, boolean enabled,boolean isSuperAdmin) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.isSuperAdmin = isSuperAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> permissions = this.getRole().getPermissions().stream().map(Enum::name).collect(Collectors.toSet());
        permissions.add(this.getRole().getType().name());
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
