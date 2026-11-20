package com.careerflow.dto;

import java.time.LocalDateTime;

/** Every error returned by the API has this shape. */
public record ErrorResponse(LocalDateTime timestamp, int status, String message) {
}
