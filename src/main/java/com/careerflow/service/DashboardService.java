package com.careerflow.service;

import com.careerflow.dto.ApplicationResponse;
import com.careerflow.dto.DashboardStats;
import com.careerflow.dto.InterviewResponse;
import com.careerflow.entity.ApplicationStatus;
import com.careerflow.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Numbers and lists for the dashboard - always calculated from the logged-in student's own records. */
@Service
public class DashboardService {

    private final ApplicationRepository applicationRepository;
    private final InterviewService interviewService;

    public DashboardService(ApplicationRepository applicationRepository, InterviewService interviewService) {
        this.applicationRepository = applicationRepository;
        this.interviewService = interviewService;
    }

    public DashboardStats getStats(String userId) {
        return new DashboardStats(
                applicationRepository.countByUserId(userId),
                count(userId, ApplicationStatus.APPLIED),
                count(userId, ApplicationStatus.SHORTLISTED),
                count(userId, ApplicationStatus.INTERVIEW),
                count(userId, ApplicationStatus.REJECTED),
                count(userId, ApplicationStatus.SELECTED),
                count(userId, ApplicationStatus.OFFER_RECEIVED));
    }

    public List<ApplicationResponse> getRecentApplications(String userId) {
        return applicationRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    public List<InterviewResponse> getUpcomingInterviews(String userId) {
        return interviewService.getUpcoming(userId).stream().limit(5).toList();
    }

    private long count(String userId, ApplicationStatus status) {
        return applicationRepository.countByUserIdAndStatus(userId, status);
    }
}
