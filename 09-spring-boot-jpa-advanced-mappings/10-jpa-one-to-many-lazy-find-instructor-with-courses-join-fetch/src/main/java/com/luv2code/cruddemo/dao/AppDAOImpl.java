package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

    // define field for entity manager
    // inject entity manager using constructor injection per Lombok
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

    @Override
    public List<Course> findCoursesByInstructorId(int theId) {

        // create query
        TypedQuery<Course> query = entityManager.createQuery(
                                    "from Course where instructor.id = :data", Course.class);
        query.setParameter("data", theId);

        // execute query
        return query.getResultList();

    }

    // Chap307
    @Override
    public Instructor findInstructorByIdJoinFetch(int theId) {
        // Get the CriteriaBuilder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Create a CriteriaQuery for the Instructor class
        CriteriaQuery<Instructor> criteriaQuery = criteriaBuilder.createQuery(Instructor.class);

        // Define the root of the query (Instructor entity)
        Root<Instructor> instructorRoot = criteriaQuery.from(Instructor.class);

        // Join with courses and instructorDetail to fetch them eagerly
        instructorRoot.fetch("courses"); // Assuming "courses" is the name of the relationship
        instructorRoot.fetch("instructorDetail"); // Assuming "instructorDetail" is the name of the relationship

        // Add a where clause for the instructor ID
        criteriaQuery.select(instructorRoot).where(criteriaBuilder.equal(instructorRoot.get("id"), theId));

        // Create and execute the query
        return entityManager.createQuery(criteriaQuery).getSingleResult();

    }
}







