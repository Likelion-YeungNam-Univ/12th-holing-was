package com.example.holing.bounded_context.medicine.serivce;

import com.example.holing.bounded_context.medicine.entity.Medicine;
import com.example.holing.bounded_context.medicine.entity.MedicineHistory;
import com.example.holing.bounded_context.medicine.repository.MedicineHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineHistoryService {

    private final MedicineService medicineService;
    private final MedicineHistoryRepository medicineHistoryRepository;

    public MedicineHistory create(Medicine medicine) {
        MedicineHistory medicineHistory = MedicineHistory.builder()
                .medicine(medicine)
                .createdAt(LocalDateTime.now())
                .build();
        return medicineHistoryRepository.save(medicineHistory);
    }

    public Optional<MedicineHistory> check(Long medicineId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        return medicineHistoryRepository.findByMedicineAndCreatedAtToday(medicineId, startOfDay, endOfDay);
    }

    public void taken(Long medicineId) {
        if (check(medicineId).isEmpty()) {
            Medicine medicine = medicineService.readById(medicineId);
            create(medicine);
        }
    }

    public void skip(Long medicineId) {
        check(medicineId).ifPresent(medicineHistoryRepository::delete);
    }
}
