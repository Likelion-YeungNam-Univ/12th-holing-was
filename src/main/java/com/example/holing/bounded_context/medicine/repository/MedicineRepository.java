package com.example.holing.bounded_context.medicine.repository;

import com.example.holing.bounded_context.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    @Query(value = "SELECT m.*, mh.id FROM medicine m " +
            "LEFT JOIN medicine_history mh ON m.id = mh.medicine_id " +
            "AND DATE(mh.created_at) = CURRENT_DATE " +
            "WHERE m.user_id = :userId", nativeQuery = true)
    List<Object[]> findAllByUserId(Long userId);
}
