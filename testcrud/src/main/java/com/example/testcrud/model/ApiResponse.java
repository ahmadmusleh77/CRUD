package com.example.testcrud.model;

import java.time.LocalDateTime;

public class ApiResponse {
    private final int status;
    private final String message;
    private final Object data;
    private final LocalDateTime timestamp;

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}