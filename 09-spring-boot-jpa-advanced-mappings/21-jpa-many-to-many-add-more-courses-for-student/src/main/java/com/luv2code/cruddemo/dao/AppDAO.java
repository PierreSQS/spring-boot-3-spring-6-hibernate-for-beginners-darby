package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import com.luv2code.cruddemo.entity.Student;

import java.util.List;

public interface AppDAO {

    void saveInstructor(Instructor theInstructor);

    Instructor findInstructorById(int theId);

    void deleteInstructorById(int theId);

    InstructorDetail findInstructorDetailById(int theId);

    void deleteInstructorDetailById(int theId);

    List<Course> findCoursesByInstructorID(int theID);

    Instructor findInstructorByIdJoinFetch(int theID);

    void updateInstructor(Instructor instructorUpdate);

    void updateCourse(Course courseUpdate);

    Course findCourseByID(int theID);

    void deleteCourseByID(int theID);

    void saveCourse(Course course);

    Course findCourseAndReviewsByCourseId(int theID);

    List<Course> getAllCourses();

    Course findCourseAndStudentsByCourseID(int courseID);

    Student findStudentAndCoursesByStudentID(int studentID);

    void updateStudent(Student studentUpdate);
}













