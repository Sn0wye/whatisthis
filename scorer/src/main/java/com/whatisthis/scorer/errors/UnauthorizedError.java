package com.whatisthis.scorer.errors;

public class UnauthorizedError {
    private final int code = 401;
    private final String message;

    public UnauthorizedError(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
