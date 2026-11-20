package com.careerflow.service;

import com.careerflow.dto.ApplicationRequest;
import com.careerflow.dto.ApplicationResponse;
import com.careerflow.entity.Application;
import com.careerflow.entity.ApplicationStatus;
import com.careerflow.entity.JobType;
import com.careerflow.exception.ResourceNotFoundException;
import com.careerflow.repository.ApplicationRepository;
import com.careerflow.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/** Business logic for job applications. Every method receives the logged-in userId. */
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public ApplicationService(ApplicationRepository applicationRepository, InterviewRepository interviewRepository) {
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    public ApplicationResponse create(String userId, ApplicationRequest request) {
        Application application = new Application();
        application.setUserId(userId);              // owner = logged-in student
        copyFields(application, request);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        return ApplicationResponse.from(applicationRepository.save(application));
    }

    /** All applications of this student, optionally filtered by search text and/or status. */
    public List<ApplicationResponse> getAll(String userId, String search, ApplicationStatus status) {
        List<Application> applications;
        if (search != null && !search.isBlank()) {
            // Pattern.quote makes the text literal so characters like "." or "(" are not treated as regex
            applications = applicationRepository.searchByKeyword(userId, Pattern.quote(search.trim()));
        } else {
            applications = applicationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        return applications.stream()
                .filter(a -> status == null || a.getStatus() == status)
                .map(ApplicationResponse::from)
                .toList();
    }

    public List<ApplicationResponse> getByStatus(String userId, ApplicationStatus status) {
        return applicationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    public ApplicationResponse getById(String userId, String id) {
        return ApplicationResponse.from(findOwned(userId, id));
    }

    public ApplicationResponse update(String userId, String id, ApplicationRequest request) {
        Application application = findOwned(userId, id);
        copyFields(application, request);
        application.setUpdatedAt(LocalDateTime.now());
        return ApplicationResponse.from(applicationRepository.save(application));
    }

    public void delete(String userId, String id) {
        Application application = findOwned(userId, id);
        interviewRepository.deleteByApplicationIdAndUserId(application.getId(), userId); // remove its interviews
        applicationRepository.delete(application);
    }

    /** Finds by id AND userId. Another student's record is reported as "not found". */
    private Application findOwned(String userId, String id) {
        return applicationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private void copyFields(Application application, ApplicationRequest request) {
        application.setCompanyName(request.companyName().trim());
        application.setJobTitle(request.jobTitle().trim());
        application.setJobDescription(request.jobDescription());
        application.setLocation(request.location());
        application.setJobType(request.jobType() != null ? request.jobType() : JobType.FULL_TIME);
        application.setJobUrl(request.jobUrl());
        application.setAppliedDate(request.appliedDate());
        application.setStatus(request.status() != null ? request.status() : ApplicationStatus.APPLIED);
        application.setNotes(request.notes());
    }
}
