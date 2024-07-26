package com.example.holing.bounded_context.medicine.repository;

import com.example.holing.bounded_context.medicine.entity.Medicine;
import com.example.holing.bounded_context.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("select m from Medicine m " +
            "left join fetch m.medicineHistoryList " +
            "where m.user = :user")
    List<Medicine> findAllByUser(User user);

//    @Query(value = "SELECT m.id, m.name, m.taken_at, mh.id " +
//            "FROM medicine m " +
//            "LEFT JOIN medicine_history mh ON m.id = mh.medicine_id " +
//            "AND DATE(mh.created_at) = CURRENT_DATE " +
//            "WHERE m.user_id = :userId", nativeQuery = true)
//    List<Object[]> findAllByUserId(Long userId);


//    @Query(value = "SELECT m.* FROM medicine m " +
//            "LEFT JOIN medicine_history mh ON m.id = mh.medicine_id " +
//            "AND DATE(mh.created_at) = CURRENT_DATE ", nativeQuery = true)
//    List<Medicine> findAllByUserId(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
