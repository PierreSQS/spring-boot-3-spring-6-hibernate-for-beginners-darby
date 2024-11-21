package com.luv2code.demo.rest;

import com.luv2code.demo.entity.Student;
import com.luv2code.demo.error.StudentErrorResponse;
import com.luv2code.demo.exception.StudentNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> theStudents;

    // define @PostConstruct to load the student data ... only once!

    @PostConstruct
    public void loadData() {

        theStudents = new ArrayList<>();

        theStudents.add(new Student("Poornima", "Patel"));
        theStudents.add(new Student("Mario", "Rossi"));
        theStudents.add(new Student("Mary", "Smith"));
    }


    // define endpoint for "/students" - return a list of students

    @GetMapping("/students")
    public List<Student> getStudents() {

        return theStudents;
    }

    @GetMapping("students/{studentID}")
    public Student getStudent(@PathVariable int studentID) {
        if (studentID > theStudents.size() || studentID < 0)
            throw new StudentNotFoundException("Student with " +studentID+ " not found !");
        return theStudents.get(studentID);
    }

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







