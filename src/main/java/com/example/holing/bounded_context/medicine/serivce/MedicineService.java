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

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Transactional
    public Medicine create(User user, Medicine medicine) {
        medicine.setUser(user);
        return medicineRepository.save(medicine);
    }

    public List<Object[]> readAll(Long userId) {
        return medicineRepository.findAllByUserId(userId);
    }

    public Medicine readById(Long medicineId) {
        return medicineRepository.findById(medicineId)
                .orElseThrow(() -> new GlobalException(MedicineExceptionCode.MEDICINE_NOT_FOUND));
    }
}
