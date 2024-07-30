package com.example.holing.bounded_context.mission.api;

import com.example.holing.bounded_context.mission.dto.MissionCountDto;
import com.example.holing.bounded_context.mission.dto.MissionResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/missions")
@Tag(name = "[미션 관련 API]", description = "미션 기능에 대한 API")
public interface MissionApi {

    @GetMapping("")
    @Operation(summary = "미션 생성", description = "미션을 생성하기 위한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 생성 성공"),
            @ApiResponse(responseCode = "409", description = "미션 생성 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-26T17:30:09.9259602",
                                                                                    "name": "ALREADY_EXISTS",
                                                                                    "cause": "미션이 최신입니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<?> create(HttpServletRequest request);

    @GetMapping("/history")
    @Operation(summary = "미션 조회", description = "특정 날짜의 미션 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 조회 성공")
    })
    ResponseEntity<?> read(HttpServletRequest request, @RequestParam String date);

    @PatchMapping("/{missionResultId}")
    @Operation(summary = "미션 교체", description = "선택한 미션을 동일한 태그내의 다른 미션으로 교체합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 교체 성공"),
            @ApiResponse(responseCode = "400", description = "미션 교체 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-30T22:51:29.3950973",
                                                                                    "name": "ACCESS_DENIED",
                                                                                    "cause": "이미 지난 미션입니다."
                                                                                }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "최신 미션이 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-30T22:51:29.3950973",
                                                                                    "name": "NOT_EXIST_MISSION",
                                                                                    "cause": "미션이 존재하지 않습니다."
                                                                                }
                                    """),
                    })),
            @ApiResponse(responseCode = "409", description = "미션 교체 중복 요청",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-30T22:52:02.7411103",
                                                                                    "name": "ALREADY_UPDATED",
                                                                                    "cause": "이미 미션을 교체하셨습니다."
                                                                                }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "완료된 미션에 대해 교체 요청",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-30T22:52:24.7846393",
                                                                                    "name": "ALREADY_COMPLETED",
                                                                                    "cause": "이미 완료된 미션입니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<MissionResultResponseDto> update(HttpServletRequest request, @PathVariable Long missionResultId);

    @PatchMapping("/{missionResultId}/complete")
    @Operation(summary = "미션 완료 처리", description = "선택한 미션의 상태를 완료 상태로 변경하고, 리워드를 받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 처리 성공"),
            @ApiResponse(responseCode = "409", description = "미션 완료 중복 요청",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-30T22:52:24.7846393",
                                                                                    "name": "ALREADY_COMPLETED",
                                                                                    "cause": "이미 완료된 미션입니다."
                                                                                }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "과거 미션 완료 요청",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-30T22:52:51.3695759",
                                                                                    "name": "ACCESS_DENIED",
                                                                                    "cause": "이미 지난 미션입니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<MissionResultResponseDto> complete(HttpServletRequest request, @PathVariable Long missionResultId);

    @GetMapping("/month")
    @Operation(summary = "완료된 미션 개수 반환", description = "해당 날짜의 월에 완료한 미션의 개수를 날짜별로 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "날짜별 완료된 미션 개수 반환 성공")
    })
    ResponseEntity<List<MissionCountDto>> getMissionCount(HttpServletRequest request, @RequestParam LocalDate date);
}

