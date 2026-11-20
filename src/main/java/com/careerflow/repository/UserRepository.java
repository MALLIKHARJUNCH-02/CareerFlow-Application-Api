package com.careerflow.repository;

import com.careerflow.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/** Spring Data creates the implementation automatically from the method names. */
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
