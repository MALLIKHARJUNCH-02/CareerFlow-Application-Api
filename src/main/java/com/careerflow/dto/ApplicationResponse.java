package com.careerflow.dto;

import com.careerflow.entity.Application;
import com.careerflow.entity.ApplicationStatus;
import com.careerflow.entity.JobType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationResponse(
        String id, String companyName, String jobTitle, String jobDescription, String location,
        JobType jobType, String jobUrl, LocalDate appliedDate, ApplicationStatus status, String notes,
        LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static ApplicationResponse from(Application a) {
        return new ApplicationResponse(a.getId(), a.getCompanyName(), a.getJobTitle(), a.getJobDescription(),
                a.getLocation(), a.getJobType(), a.getJobUrl(), a.getAppliedDate(), a.getStatus(),
                a.getNotes(), a.getCreatedAt(), a.getUpdatedAt());
    }
}
