package org.example.plantdisease.repository;


import org.example.plantdisease.entity.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FruitRepository extends JpaRepository<Fruit, Long> {

    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name,Long id);
}
