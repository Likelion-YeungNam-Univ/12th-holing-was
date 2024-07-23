package com.example.holing.bounded_context.schedule.api;

import com.example.holing.bounded_context.schedule.dto.ScheduleRequestDto;
import com.example.holing.bounded_context.schedule.dto.ScheduleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/calendar")
@Tag(name = "[캘린더 관련 일정 API]", description = "캘린더 기능의 일정 관련 API")
public interface ScheduleApi {

    @GetMapping("/schedules")
    @Operation(summary = "선택한 날짜의 일정을 조회", description = "선택한 날짜의 일정을 조회하기 위한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"),
            @ApiResponse(responseCode = "404", description = "일정 조회 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-24T00:30:09.9259602",
                                                                                    "name": "SCHEDULE_NOT_FOUND",
                                                                                    "cause": "일정을 찾을 수 없습니다."
                                                                                }
                                    """),
                    }))
    })
    ResponseEntity<List<ScheduleResponseDto>> getDateSchedule(HttpServletRequest request, @RequestParam("date") String date);

    @PostMapping("/schedules")
    @Operation(summary = "선택한 날짜의 일정을 생성", description = "선택한 날짜의 일정을 생성하기 위한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 생성 성공"),
            @ApiResponse(responseCode = "404", description = "일정 생성 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-24T00:30:09.9259602",
                                                                                    "name": "INVALID_DATETIME",
                                                                                    "cause": "잘못된 일정입니다."
                                                                                }
                                    """),
                    })),
    })
    public ResponseEntity<ScheduleResponseDto> createSchedule(HttpServletRequest request, @RequestBody ScheduleRequestDto scheduleRequestDto);

    @PutMapping("/schedules/{scheduleId}")
    @Operation(summary = "선택한 날짜의 일정을 수정", description = "선택한 날짜의 일정을 수정하기 위한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
            @ApiResponse(responseCode = "404", description = "일정 수정 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-24T00:30:09.9259602",
                                                                                    "name": "SCHEDULE_NOT_FOUND",
                                                                                    "cause": "일정을 찾을 수 없습니다."
                                                                                }
                                    """),
                    }))
    })
    public ResponseEntity<ScheduleResponseDto> updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto);

    @DeleteMapping("/schedules/{scheduleId}")
    @Operation(summary = "선택한 날짜의 일정을 삭제", description = "선택한 날짜의 일정을 삭제하기 위한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "일정 삭제 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                                                                {
                                                                                    "timeStamp": "2024-07-24T00:30:09.9259602",
                                                                                    "name": "SCHEDULE_NOT_FOUND",
                                                                                    "cause": "일정을 찾을 수 없습니다."
                                                                                }
                                    """),
                    }))
    })
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId);
}
