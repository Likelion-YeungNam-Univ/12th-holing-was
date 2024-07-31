package com.example.holing.bounded_context.report.repository;

import com.example.holing.bounded_context.report.entity.UserReport;
import com.example.holing.bounded_context.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    /**
     * 사용자의 최신 리포트를 조회하기 위한 쿼리<br>
     * 리포트 생성 전 최신 리포트의 생성날짜를 조회하기 위한 용도로 사용합니다.
     *
     * @param user
     * @return
     */
    Optional<UserReport> findFirstByUserOrderByCreatedAtDesc(User user);

    /**
     * 사용자의 모든 리포트를 최신순으로 조회하기 위한 쿼리<br>
     * 리포트 요약에서 리포트와 솔루션을 조회하기 위한 용도로 사용합니다.
     *
     * @param userId
     * @return
     */
    @Query("select ur from UserReport ur " +
            "join fetch ur.reports r " +
            "join fetch r.solution " +
            "where ur.user.id = :userId")
    List<UserReport> findTop4WithReportAndSolutionByUser(Long userId, Pageable pageable);

    /**
     * 특정 리포트를 조회하기 위한 쿼리<br>
     * 리포트 상세보기에서 사용자 리포트, 리포트, 솔루션의 정보를 출력하기 위한 용도로 사용합니다.
     *
     * @param id
     * @return
     */
    @Query("select ur from UserReport ur " +
            "join fetch ur.reports r " +
            "join fetch r.solution " +
            "where ur.id = :id")
    Optional<UserReport> findWithReportAndSolutionById(Long id);

    /**
     * 사용자의 모든 리포트를 과거순으로 조회하기 위한 쿼리<br>
     * 사용자 리포트 그래프에서 사용자의 리포트, 리포트의 정보를 출력하기 위해 사용합니다.
     *
     * @param userId
     * @return
     */
    @Query("select ur from UserReport ur " +
            "join fetch ur.reports r " +
            "where ur.user.id = :userId " +
            "order by ur.createdAt asc"
    )
    List<UserReport> findAllWithReportByUser(Long userId);
}
