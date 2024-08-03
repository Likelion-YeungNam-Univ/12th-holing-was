package com.example.holing.bounded_context.medicine.api;

import com.example.holing.bounded_context.medicine.dto.MedicineRequestDto;
import com.example.holing.bounded_context.medicine.dto.MedicineResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import java.util.List;

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
    @ApiResponse(responseCode = "200", description = "영양제 목록 조회 성공")
    ResponseEntity<List<MedicineResponseDto>> read(HttpServletRequest request);

    @PostMapping("/{medicineId}")
    @Operation(summary = "영양제 복용 기록 생성", description = "사용자가 영양제를 복용했다는 기록을 생성하기 위한 API 입니다.")
    @ApiResponse(responseCode = "200", description = "영양제 복용 기록 생성 성공")
    @ApiResponse(responseCode = "403", description = "해당 영양제의 작성자가 아닌 경우",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                                                                        {
                                                                            "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                            "name": "ACCESS_DENIED_TO_MEDICINE",
                                                                            "cause": "해당 영양제에 접근 권한이 없습니다."
                                                                        }
                            """),
            }))
    @ApiResponse(responseCode = "404", description = "영양제가 존재하지 않는 경우",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                                                                        {
                                                                            "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                            "name": "MEDICINE_NOT_FOUND",
                                                                            "cause": "해당 영양제가 존재하지 않습니다."
                                                                        }
                            """),
            }))
    ResponseEntity<String> taken(HttpServletRequest request, @PathVariable Long medicineId);

    @DeleteMapping("/{medicineId}")
    @Operation(summary = "영양제 복용 기록 삭제", description = "사용자가 영양제 복용으로 생성된 영양제 복용 기록을 삭제하기 위한 API 입니다.")
    @ApiResponse(responseCode = "200", description = "영양제 복용 기록 삭제 성공")
    @ApiResponse(responseCode = "403", description = "해당 영양제의 작성자가 아닌 경우",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                                                                        {
                                                                            "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                            "name": "ACCESS_DENIED_TO_MEDICINE",
                                                                            "cause": "해당 영양제에 접근 권한이 없습니다."
                                                                        }
                            """),
            }))
    @ApiResponse(responseCode = "404", description = "영양제가 존재하지 않는 경우",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(value = """
                                                                        {
                                                                            "timestamp": "2024-07-19T17:56:39.188+00:00",
                                                                            "name": "MEDICINE_NOT_FOUND",
                                                                            "cause": "해당 영양제가 존재하지 않습니다."
                                                                        }
                            """),
            }))
    ResponseEntity<String> skip(HttpServletRequest request, @PathVariable Long medicineId);
}
