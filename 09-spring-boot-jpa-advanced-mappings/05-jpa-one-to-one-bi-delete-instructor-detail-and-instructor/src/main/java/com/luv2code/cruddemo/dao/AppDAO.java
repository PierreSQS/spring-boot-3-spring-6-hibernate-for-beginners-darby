package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;

public interface AppDAO {

    void save(Instructor theInstructor);

    Instructor findInstructorById(int theId);

    // introduced in Sec9_Chap286
    void deleteInstructorById(int theId);

    // introduced in Sec9_Chap288
    InstructorDetail findInstructorDetailById(int theId);

}
