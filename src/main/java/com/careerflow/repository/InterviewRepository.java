package com.careerflow.repository;

import com.careerflow.entity.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends MongoRepository<Interview, String> {

    List<Interview> findByUserIdOrderByInterviewDateAscInterviewTimeAsc(String userId);

    Optional<Interview> findByIdAndUserId(String id, String userId);

    List<Interview> findByUserIdAndInterviewDateGreaterThanEqualOrderByInterviewDateAscInterviewTimeAsc(
            String userId, LocalDate fromDate);

    /** Used when an application is deleted, so its interviews are removed too. */
    void deleteByApplicationIdAndUserId(String applicationId, String userId);
}
