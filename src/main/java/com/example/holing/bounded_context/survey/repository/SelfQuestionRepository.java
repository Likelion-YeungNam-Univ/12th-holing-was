package com.example.holing.bounded_context.survey.repository;

import com.example.holing.bounded_context.survey.entity.SelfQuestion;
import com.example.holing.bounded_context.survey.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfQuestionRepository extends JpaRepository<SelfQuestion, Long> {
    /**
     * 타입들을 포함한 문항을 조회하는 쿼리<br>
     * 남성의 경우 여성 문항을 제외하는 용도로 사용합니다.
     *
     * @return
     */
    Page<SelfQuestion> findAllByTypeNot(Type type, Pageable pageable);
}
