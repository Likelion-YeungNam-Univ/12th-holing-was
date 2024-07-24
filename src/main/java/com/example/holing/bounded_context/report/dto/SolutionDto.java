package com.example.holing.bounded_context.report.dto;

import com.example.holing.bounded_context.survey.entity.Solution;
import io.swagger.v3.oas.annotations.media.Schema;

public record SolutionDto(
        @Schema(description = "요약", example = "갱년기 동안 신체 통증은 여성의 경우 에스트로겐 감소가, 남성의 경우 테스토스테론 감소가 관절의 염증을 증가시키고 근육의 유연성을 감소시켜 유발될 수 있어요. 이러한 통증은 일상 생활의 질을 크게 저하시킬 수 있어요.")
        String summary,
        @Schema(description = "콘텐츠1", example = "전문의의 진단과 치료를 받아보세요. 약물 치료나 특정 운동 요법을 통해 통증을 관리하세요. 또한, 통증이 심할 때는 무리하지 않도록 주의하세요.")
        String content1,
        @Schema(description = "콘텐츠2", example = "MSM(메틸설포닐메탄)과 보스웰리아를 섭취해보세요. MSM은 통증 완화에 도움을 주고, 보스웰리아는 항염증 작용이 있어요.")
        String content2,
        @Schema(description = "콘텐츠3", example = "녹색 잎채소와 아보카도를 드셔보세요. 녹색 잎채소는 항산화 성분이 풍부하고, 아보카도는 항염증 작용을 도와줘요.")
        String content3
) {
    public static SolutionDto fromEntity(Solution solution) {
        return new SolutionDto(
                solution.getSummary(),
                solution.getContent1(),
                solution.getContent2(),
                solution.getContent3()
        );
    }

}
