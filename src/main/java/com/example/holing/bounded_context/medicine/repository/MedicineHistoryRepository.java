package com.example.holing.bounded_context.medicine.repository;

import com.example.holing.bounded_context.medicine.entity.MedicineHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MedicineHistoryRepository extends JpaRepository<MedicineHistory, Long> {
    @Query("select mh from MedicineHistory mh " +
            "where mh.medicine.id = :medicineId " +
            "AND mh.createdAt BETWEEN :startOfDay AND :endOfDay")
    Optional<MedicineHistory> findByMedicineAndCreatedAtToday(Long medicineId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
