package com.example.holing.bounded_context.medicine.serivce;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.medicine.entity.Medicine;
import com.example.holing.bounded_context.medicine.exception.MedicineExceptionCode;
import com.example.holing.bounded_context.medicine.repository.MedicineRepository;
import com.example.holing.bounded_context.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Transactional
    public Medicine create(User user, Medicine medicine) {
        medicine.setUser(user);
        return medicineRepository.save(medicine);
    }

    @Transactional
    public Medicine delete(Long userId, Long medicineId) {
        Medicine medicine = readById(medicineId);
        if (!Objects.equals(medicine.getUser().getId(), userId))
            throw new GlobalException(MedicineExceptionCode.ACCESS_DENIED_TO_MEDICINE);
        medicineRepository.delete(medicine);
        return medicine;
    }

    public List<Object[]> readAll(Long userId) {
        return medicineRepository.findAllByUserId(userId);
    }

    public Medicine readById(Long medicineId) {
        return medicineRepository.findById(medicineId)
                .orElseThrow(() -> new GlobalException(MedicineExceptionCode.MEDICINE_NOT_FOUND));
    }
}
