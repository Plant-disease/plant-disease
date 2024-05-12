package org.example.plantdisease.repository;


import org.example.plantdisease.entity.LeafMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LeafMeasurementRepository extends JpaRepository<LeafMeasurement, Long> {

}
