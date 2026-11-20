package com.careerflow.controller;

import com.careerflow.dto.InterviewRequest;
import com.careerflow.dto.InterviewResponse;
import com.careerflow.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InterviewResponse create(Authentication auth, @Valid @RequestBody InterviewRequest request) {
        return interviewService.create(auth.getName(), request);
    }

    @GetMapping
    public List<InterviewResponse> getAll(Authentication auth) {
        return interviewService.getAll(auth.getName());
    }

    @GetMapping("/upcoming")
    public List<InterviewResponse> getUpcoming(Authentication auth) {
        return interviewService.getUpcoming(auth.getName());
    }

    @GetMapping("/{id}")
    public InterviewResponse getById(Authentication auth, @PathVariable String id) {
        return interviewService.getById(auth.getName(), id);
    }

    @PutMapping("/{id}")
    public InterviewResponse update(Authentication auth, @PathVariable String id,
                                    @Valid @RequestBody InterviewRequest request) {
        return interviewService.update(auth.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth, @PathVariable String id) {
        interviewService.delete(auth.getName(), id);
    }
}
