package com.example.holing.bounded_context.report.repository;

import com.example.holing.bounded_context.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
