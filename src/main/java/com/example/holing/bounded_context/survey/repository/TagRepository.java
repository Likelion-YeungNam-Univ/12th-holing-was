package com.example.holing.bounded_context.survey.repository;

import com.example.holing.bounded_context.survey.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * 태그의 모든 솔루션을 조회하기 위한 쿼리<br>
     * 사용자가 테스트를 완료할 시에 태그의 점수에 따른 솔루션을 제공을 위한 로직 작성으로 인해 태그의 모든 솔루션을 읽어오는 용도로 사용합니다.
     *
     * @return
     */
    @Query("select t from Tag t join fetch t.solutions")
    List<Tag> findAllWithSolution();
}
