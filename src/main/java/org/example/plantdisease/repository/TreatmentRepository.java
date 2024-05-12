package org.example.plantdisease.repository;


import org.example.plantdisease.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    Treatment findAllByLeafConditionId(Long leafConditionId);

    Boolean existsByLeafConditionId(Long leafConditionId);

    Boolean existsByLeafConditionIdAndIdNot(Long leafConditionId,Long id);
}
