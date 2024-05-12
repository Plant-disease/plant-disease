package org.example.plantdisease.repository;


import org.example.plantdisease.entity.LeafCondition;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LeafConditionRepository extends JpaRepository<LeafCondition, Long> {

    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name,Long id);
}
