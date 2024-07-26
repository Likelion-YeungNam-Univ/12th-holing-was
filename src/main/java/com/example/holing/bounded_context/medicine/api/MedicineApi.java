package com.example.holing.bounded_context.medicine.api;

import com.example.holing.bounded_context.medicine.dto.MedicineRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "[영양제 관련 API]", description = "영양제 조회, 생성 및 기록 변경 API")
@RequestMapping("/user/medicines")
public interface MedicineApi {
    @PostMapping("")
    @Operation(summary = "영양제 생성", description = "사용자가 영양제를 생성하기 위한 API 입니다.")
    ResponseEntity<String> create(HttpServletRequest request, @Valid @RequestBody MedicineRequestDto medicineRequestDto);

    @GetMapping("")
    @Operation(summary = "영양제 목록 조회", description = """
            사용자가 영양제 목록을 조회하기 위한 API 입니다.
            영양제 복용 기록을 통해 상태를 설정합니다.
            """)
    public ResponseEntity<?> read(HttpServletRequest request);

    @PostMapping("/{medicineId}/taken")
    @Operation(summary = "영양제 복용 기록 생성", description = "사용자가 영양제를 복용했다는 기록을 생성하기 위한 API 입니다.")
    public ResponseEntity<String> taken(HttpServletRequest request, @PathVariable Long medicineId);

    @DeleteMapping("/{medicineId}/skip")
    @Operation(summary = "영양제 복용 기록 삭제", description = "사용자가 영양제 복용으로 생성된 영양제 복용 기록을 삭제하기 위한 API 입니다.")
    public ResponseEntity<String> skip(HttpServletRequest request, @PathVariable Long medicineId);
}
