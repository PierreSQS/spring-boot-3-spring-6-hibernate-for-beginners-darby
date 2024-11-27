package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Student;

import java.util.List;

public interface StudentDAO {

    void save(Student theStudent);

    Student findStudentByID(Integer id);

    List<Student> findAllEmployees();

    List<Student> findAllEmplployeesByLastName(String lastName);

}
