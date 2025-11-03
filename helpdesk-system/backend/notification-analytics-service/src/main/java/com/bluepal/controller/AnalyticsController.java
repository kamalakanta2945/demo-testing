package com.bluepal.controller;

import com.bluepal.entity.DashboardMetric;
import com.bluepal.repository.AnalyticsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsController(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<DashboardMetric>> getMetrics() {
        return ResponseEntity.ok(analyticsRepository.findAll());
    }
}
