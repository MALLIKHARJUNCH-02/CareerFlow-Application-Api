package com.careerflow.dto;

import com.careerflow.entity.InterviewMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record InterviewRequest(
        @NotBlank(message = "Please select a job application") String applicationId,
        @NotNull(message = "Interview date is required") LocalDate interviewDate,
        @NotNull(message = "Interview time is required") LocalTime interviewTime,
        String interviewRound,
        InterviewMode interviewMode,
        String meetingLink,
        String notes) {
}
