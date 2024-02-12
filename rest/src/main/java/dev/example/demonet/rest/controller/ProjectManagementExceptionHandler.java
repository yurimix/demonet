package dev.example.demonet.rest.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.example.demonet.service.exception.EntityDuplicateException;
import dev.example.demonet.service.exception.EntityNotFoundException;

@ControllerAdvice
public class ProjectManagementExceptionHandler extends ResponseEntityExceptionHandler {

    public record ErrorMessage(String message) {
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request) {
        return handleException(ex, request, NOT_FOUND);
    }

    @ExceptionHandler({ EntityDuplicateException.class })
    public ResponseEntity<Object> handleDuplicateProjectException(Exception ex, WebRequest request) {
        return handleException(ex, request, BAD_REQUEST);
    }

    private ResponseEntity<Object> handleException(Exception ex, WebRequest request, HttpStatus status) {
        return new ResponseEntity<Object>(new ErrorMessage(ex.getMessage()), new HttpHeaders(), status);
    }

}
