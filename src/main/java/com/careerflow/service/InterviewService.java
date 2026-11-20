package com.careerflow.service;

import com.careerflow.dto.InterviewRequest;
import com.careerflow.dto.InterviewResponse;
import com.careerflow.entity.Application;
import com.careerflow.entity.Interview;
import com.careerflow.entity.InterviewMode;
import com.careerflow.exception.ResourceNotFoundException;
import com.careerflow.repository.ApplicationRepository;
import com.careerflow.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Business logic for interview schedules. */
@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    public InterviewService(InterviewRepository interviewRepository, ApplicationRepository applicationRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
    }

    public InterviewResponse create(String userId, InterviewRequest request) {
        Application application = findOwnedApplication(userId, request.applicationId());

        Interview interview = new Interview();
        interview.setUserId(userId);
        interview.setApplicationId(application.getId());
        copyFields(interview, request);
        interview.setCreatedAt(LocalDateTime.now());

        return InterviewResponse.from(interviewRepository.save(interview), application);
    }

    public List<InterviewResponse> getAll(String userId) {
        return toResponses(userId, interviewRepository.findByUserIdOrderByInterviewDateAscInterviewTimeAsc(userId));
    }

    public InterviewResponse getById(String userId, String id) {
        Interview interview = findOwned(userId, id);
        Application application = applicationRepository.findByIdAndUserId(interview.getApplicationId(), userId)
                .orElse(null);
        return InterviewResponse.from(interview, application);
    }

    public InterviewResponse update(String userId, String id, InterviewRequest request) {
        Interview interview = findOwned(userId, id);
        Application application = findOwnedApplication(userId, request.applicationId());

        interview.setApplicationId(application.getId());
        copyFields(interview, request);

        return InterviewResponse.from(interviewRepository.save(interview), application);
    }

    public void delete(String userId, String id) {
        interviewRepository.delete(findOwned(userId, id));
    }

    /** Interviews whose date and time are now or in the future. */
    public List<InterviewResponse> getUpcoming(String userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Interview> upcoming = interviewRepository
                .findByUserIdAndInterviewDateGreaterThanEqualOrderByInterviewDateAscInterviewTimeAsc(userId, LocalDate.now())
                .stream()
                .filter(i -> !LocalDateTime.of(i.getInterviewDate(), i.getInterviewTime()).isBefore(now))
                .toList();
        return toResponses(userId, upcoming);
    }

    // ---------- helpers ----------

    private Interview findOwned(String userId, String id) {
        return interviewRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    private Application findOwnedApplication(String userId, String applicationId) {
        return applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private void copyFields(Interview interview, InterviewRequest request) {
        interview.setInterviewDate(request.interviewDate());
        interview.setInterviewTime(request.interviewTime());
        interview.setInterviewRound(request.interviewRound());
        interview.setInterviewMode(request.interviewMode() != null ? request.interviewMode() : InterviewMode.ONLINE);
        interview.setMeetingLink(request.meetingLink());
        interview.setNotes(request.notes());
    }

    /** Adds company name / job title to each interview with ONE extra query (not one per interview). */
    private List<InterviewResponse> toResponses(String userId, List<Interview> interviews) {
        Map<String, Application> applicationsById = applicationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .collect(Collectors.toMap(Application::getId, Function.identity()));

        return interviews.stream()
                .map(i -> InterviewResponse.from(i, applicationsById.get(i.getApplicationId())))
                .toList();
    }
}
