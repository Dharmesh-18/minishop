package com.dharmesh.minishop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {

    private final int status;
    private final String message;
    private final String details;
    private final Instant timestamp;


}
