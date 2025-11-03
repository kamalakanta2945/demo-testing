package com.bluepal.repository;

import com.bluepal.entity.DashboardMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface AnalyticsRepository extends JpaRepository<DashboardMetric, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DashboardMetric> findById(String id);
}
