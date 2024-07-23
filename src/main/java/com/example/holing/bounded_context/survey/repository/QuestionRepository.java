package com.example.holing.bounded_context.survey.repository;

import com.example.holing.bounded_context.survey.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    /**
     * 월경 태그를 제외한 문항을 조회하는 쿼리<br>
     * 남성 또는 비월경 여성의 경우 월경 태그의 문항을 제외하는 용도로 사용합니다.
     *
     * @return
     */
    @Query("select q from Question q join fetch q.tag t where t.id != 6")
    List<Question> findAllByTagNotPeriod();
}
