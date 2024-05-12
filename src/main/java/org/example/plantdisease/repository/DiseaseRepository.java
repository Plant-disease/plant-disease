package org.example.plantdisease.repository;


import org.example.plantdisease.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name,Long id);
}
