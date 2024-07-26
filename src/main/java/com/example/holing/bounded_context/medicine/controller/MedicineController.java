package com.example.holing.bounded_context.medicine.controller;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.medicine.dto.MedicineRequestDto;
import com.example.holing.bounded_context.medicine.dto.MedicineResponseDto;
import com.example.holing.bounded_context.medicine.entity.Medicine;
import com.example.holing.bounded_context.medicine.serivce.MedicineHistoryService;
import com.example.holing.bounded_context.medicine.serivce.MedicineService;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/medicines")
public class MedicineController {

    private final MedicineService medicineService;
    private final MedicineHistoryService medicineHistoryService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<String> create(HttpServletRequest request, @RequestBody MedicineRequestDto medicineRequestDto) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));

        Medicine response = medicineService.create(user, MedicineRequestDto.toEntity(medicineRequestDto));
        return ResponseEntity.ok().body("약이 성공적으로 생성되었습니다.");
    }

    @GetMapping("")
    public ResponseEntity<?> read(HttpServletRequest request) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));

        List<Medicine> medicines = medicineService.readAll(user);
        List<MedicineResponseDto> response = medicines.stream().map(MedicineResponseDto::fromEntity).toList();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{medicineId}/taken")
    public ResponseEntity<String> taken(HttpServletRequest request, @PathVariable Long medicineId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        medicineHistoryService.taken(medicineId);
        return ResponseEntity.ok("약 복용 기록이 저장되었습니다.");
    }

    @DeleteMapping("/{medicineId}/skip")
    public ResponseEntity<String> skip(HttpServletRequest request, @PathVariable Long medicineId) {
        String accessToken = jwtProvider.getToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        User user = userService.read(Long.parseLong(userId));
        medicineHistoryService.skip(medicineId);
        return ResponseEntity.ok("약 복용 기록이 삭제되었습니다.");
    }
}
