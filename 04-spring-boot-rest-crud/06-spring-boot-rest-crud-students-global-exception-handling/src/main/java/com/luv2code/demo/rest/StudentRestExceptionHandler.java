package com.luv2code.demo.rest;

import com.luv2code.demo.error.StudentErrorResponse;
import com.luv2code.demo.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class StudentRestExceptionHandler {

    // Add an Exception Handler for NFE
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException sNFE) {
        // create a ResponseError
        StudentErrorResponse studentErrResp = new StudentErrorResponse();

        // return the Response Entity
        studentErrResp.setStatus(HttpStatus.NOT_FOUND.value());
        studentErrResp.setMessage(sNFE.getMessage());
        studentErrResp.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.of(Optional.of(studentErrResp));
    }

    // Add another Exception Handler to catch any exception
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception e) {
        // create a ResponseError
        StudentErrorResponse studentErrResp = new StudentErrorResponse();

        // return the Response Entity
        studentErrResp.setStatus(HttpStatus.BAD_REQUEST.value());
        studentErrResp.setMessage(e.getMessage());
        studentErrResp.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.of(Optional.of(studentErrResp));
    }
}
