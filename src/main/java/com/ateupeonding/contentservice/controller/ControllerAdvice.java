package com.ateupeonding.contentservice.controller;

import com.ateupeonding.contentservice.model.dto.error.ErrorResponse;
import com.ateupeonding.contentservice.model.error.ContentServiceException;
import com.ateupeonding.contentservice.model.error.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ControllerAdvice {

    @Autowired
    private HttpServletRequest request;



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        String login = (String) request.getAttribute("USER_LOGIN");
        response.setUser(login);
        response.setCreatedTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ContentServiceException.class)
    public ResponseEntity<ErrorResponse> handlerUserServiceException(ContentServiceException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        String login = (String) request.getAttribute("USER_LOGIN");
        response.setUser(login);
        response.setCreatedTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
