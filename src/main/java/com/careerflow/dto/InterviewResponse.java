package com.careerflow.dto;

import com.careerflow.entity.Application;
import com.careerflow.entity.Interview;
import com.careerflow.entity.InterviewMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** Interview details plus the company name and job title of its application (handy for the UI). */
public record InterviewResponse(
        String id, String applicationId, String companyName, String jobTitle,
        LocalDate interviewDate, LocalTime interviewTime, String interviewRound,
        InterviewMode interviewMode, String meetingLink, String notes, LocalDateTime createdAt) {

    public static InterviewResponse from(Interview i, Application app) {
        return new InterviewResponse(i.getId(), i.getApplicationId(),
                app != null ? app.getCompanyName() : null,
                app != null ? app.getJobTitle() : null,
                i.getInterviewDate(), i.getInterviewTime(), i.getInterviewRound(),
                i.getInterviewMode(), i.getMeetingLink(), i.getNotes(), i.getCreatedAt());
    }
}
