package org.example.plantdisease.repository;


import org.example.plantdisease.entity.User;
import org.example.plantdisease.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findFirstByPhoneNumberAndEnabledIsTrueAndAccountNonExpiredIsTrueAndCredentialsNonExpiredIsTrueAndAccountNonLockedIsTrue(String phoneNumber);

    Optional<User> findFirstByUsernameAndEnabledIsTrueAndAccountNonExpiredIsTrueAndCredentialsNonExpiredIsTrueAndAccountNonLockedIsTrue(String phoneNumber);


    Optional<User> findByEmail(String idNumber);

    @Query(value = "SELECT id_number\n" +
            "FROM users\n" +
            "WHERE id_number NOTNULL\n" +
            "ORDER BY id_number DESC\n" +
            "LIMIT 1", nativeQuery = true)
    String findLastIdNumber();

    @Query(value = "select (count(*) > 0) as exists\n" +
            "from users\n" +
            "where id_number = (:idNumber)", nativeQuery = true)
    Boolean existsByIdNumber(@Param("idNumber") String idNumber);

//        @Query(value = "select u.*\n" +
//            "from users as u\n" +
//            "         join role on role.id = u.role_id\n" +
//            "where u.phone_number = : phoneNumber\n" +
//            "  and role.type = :roleType"
//            , nativeQuery = true)
//    Optional<User> getUserByPhoneNumberAndRoleType(
//            String phoneNumber, RoleType role_type);

    List<User> findAllByRoleTypeNot(RoleType roleType);
}
