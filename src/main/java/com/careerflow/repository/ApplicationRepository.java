package com.careerflow.repository;

import com.careerflow.entity.Application;
import com.careerflow.entity.ApplicationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/** Every method takes userId so a student only ever reaches their own data. */
public interface ApplicationRepository extends MongoRepository<Application, String> {

    List<Application> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Application> findByIdAndUserId(String id, String userId);

    List<Application> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, ApplicationStatus status);

    List<Application> findTop5ByUserIdOrderByCreatedAtDesc(String userId);

    long countByUserId(String userId);

    long countByUserIdAndStatus(String userId, ApplicationStatus status);

    /** Case-insensitive search in company name OR job title (regex is already escaped by the service). */
    @Query(value = "{ 'userId': ?0, '$or': [ { 'companyName': { '$regex': ?1, '$options': 'i' } }, "
            + "{ 'jobTitle': { '$regex': ?1, '$options': 'i' } } ] }", sort = "{ 'createdAt': -1 }")
    List<Application> searchByKeyword(String userId, String regex);
}
