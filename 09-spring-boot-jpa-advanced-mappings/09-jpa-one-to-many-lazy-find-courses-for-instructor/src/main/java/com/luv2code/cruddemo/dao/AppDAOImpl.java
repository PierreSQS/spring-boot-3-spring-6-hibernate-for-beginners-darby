package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AppDAOImpl implements AppDAO {

    // inject entity manager using constructor injection with Lombok
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void save(Instructor theInstructor) {
        entityManager.persist(theInstructor);
    }

    @Override
    public Instructor findInstructorById(int theId) {
        return entityManager.find(Instructor.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorById(int theId) {

        // retrieve the instructor
        Instructor tempInstructor = entityManager.find(Instructor.class, theId);

        // delete the instructor
        entityManager.remove(tempInstructor);
    }

    @Override
    public InstructorDetail findInstructorDetailById(int theId) {
        return entityManager.find(InstructorDetail.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorDetailById(int theId) {

        // retrieve instructor detail
        InstructorDetail tempInstructorDetail = entityManager.find(InstructorDetail.class, theId);

        // remove the associated object reference
        // break bidirectional link
        //
        tempInstructorDetail.getInstructor().setInstructorDetail(null);

        // delete the instructor detail
        entityManager.remove(tempInstructorDetail);
    }

    // INTRODUCED IN Sec9_Chap304 BUT REIMPLEMENTED WITH CriteriaQuery
    @Override
    public List<Course> findCoursesByInstructorId(int theId) {

        // Get CriteriaBuilder instance from EntityManager
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Create CriteriaQuery for Course
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        // Define the root of the query (Course entity)
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        // Add the where clause (instructor.id = :data)
        criteriaQuery.where(criteriaBuilder.equal(courseRoot.get("instructor").get("id"), theId));

        // Create and execute the query
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}







