package com.careerflow.controller;

import com.careerflow.dto.ApplicationResponse;
import com.careerflow.dto.DashboardStats;
import com.careerflow.dto.InterviewResponse;
import com.careerflow.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardStats stats(Authentication auth) {
        return dashboardService.getStats(auth.getName());
    }

    @GetMapping("/recent-applications")
    public List<ApplicationResponse> recentApplications(Authentication auth) {
        return dashboardService.getRecentApplications(auth.getName());
    }

    @GetMapping("/upcoming-interviews")
    public List<InterviewResponse> upcomingInterviews(Authentication auth) {
        return dashboardService.getUpcomingInterviews(auth.getName());
    }
}
