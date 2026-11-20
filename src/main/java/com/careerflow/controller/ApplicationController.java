package com.careerflow.controller;

import com.careerflow.dto.ApplicationRequest;
import com.careerflow.dto.ApplicationResponse;
import com.careerflow.entity.ApplicationStatus;
import com.careerflow.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse create(Authentication auth, @Valid @RequestBody ApplicationRequest request) {
        return applicationService.create(auth.getName(), request);
    }

    /** GET /api/applications?search=google&status=APPLIED  (both parameters are optional) */
    @GetMapping
    public List<ApplicationResponse> getAll(Authentication auth,
                                            @RequestParam(required = false) String search,
                                            @RequestParam(required = false) ApplicationStatus status) {
        return applicationService.getAll(auth.getName(), search, status);
    }

    @GetMapping("/status/{status}")
    public List<ApplicationResponse> getByStatus(Authentication auth, @PathVariable ApplicationStatus status) {
        return applicationService.getByStatus(auth.getName(), status);
    }

    @GetMapping("/{id}")
    public ApplicationResponse getById(Authentication auth, @PathVariable String id) {
        return applicationService.getById(auth.getName(), id);
    }

    @PutMapping("/{id}")
    public ApplicationResponse update(Authentication auth, @PathVariable String id,
                                      @Valid @RequestBody ApplicationRequest request) {
        return applicationService.update(auth.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth, @PathVariable String id) {
        applicationService.delete(auth.getName(), id);
    }
}
