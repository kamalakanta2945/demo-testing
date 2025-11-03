package com.bluepal.service;

import com.bluepal.entity.DashboardMetric;
import com.bluepal.repository.AnalyticsRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public void incrementTicketCount() {
        DashboardMetric metric = analyticsRepository.findById("total_tickets")
                .orElse(new DashboardMetric("total_tickets", 0));
        metric.setMetricValue(metric.getMetricValue() + 1);
        analyticsRepository.save(metric);
    }
}
