package com.bluepal.repository;

import com.bluepal.entity.DashboardMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<DashboardMetric, String> {
}
