package com.careerflow.dto;

import com.careerflow.entity.ApplicationStatus;
import com.careerflow.entity.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Data sent when adding or editing a job application. Note: no userId here - it comes from the JWT. */
public record ApplicationRequest(
        @NotBlank(message = "Company name is required") String companyName,
        @NotBlank(message = "Job title is required") String jobTitle,
        String jobDescription,
        String location,
        JobType jobType,
        String jobUrl,
        @NotNull(message = "Applied date is required") LocalDate appliedDate,
        ApplicationStatus status,
        String notes) {
}
