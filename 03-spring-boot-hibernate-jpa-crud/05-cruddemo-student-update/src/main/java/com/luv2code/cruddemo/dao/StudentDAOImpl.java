package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StudentDAOImpl implements StudentDAO {

    // define field for entity manager
    private final EntityManager entityManager;

    // implement save method
    @Override
    @Transactional
    public void save(Student theStudent) {
        entityManager.persist(theStudent);
    }

    @Override
    public Student findStudentByID(Integer studentID) {
        return entityManager.find(Student.class, studentID);
    }

    @Override
    public List<Student> findAllEmployees() {
        // create query
        TypedQuery<Student> studentTypedQuery = entityManager.createQuery("FROM Student", Student.class);

        // execute query and return result
        return studentTypedQuery.getResultList();
    }

    @Override
    public List<Student> findEmployeesByLastName(String lastName) {
        // create query
        TypedQuery<Student> studentTypedQuery = entityManager
                .createQuery("FROM Student where lastName=:data", Student.class);

        // Set Query Param
        studentTypedQuery.setParameter("data", lastName);

        // execute query and return result
        return studentTypedQuery.getResultList();
    }

    @Override
    public void updateStudent(Student theStudent) {
        entityManager.merge(theStudent);
    }

}










