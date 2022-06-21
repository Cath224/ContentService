package com.ateupeonding.contentservice.model.error;

public class ContentServiceException extends RuntimeException {

    public ContentServiceException(String message) {
        super(message);
    }

    public ContentServiceException(Throwable cause) {
        super(cause);
    }
}
