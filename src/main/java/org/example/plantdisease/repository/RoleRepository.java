package org.example.plantdisease.repository;


import org.example.plantdisease.entity.Role;
import org.example.plantdisease.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByType(RoleType roleType);

    List<Role> findAllByTypeNot(RoleType roleType);
}
