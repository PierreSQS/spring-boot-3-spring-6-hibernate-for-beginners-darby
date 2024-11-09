package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AppDAOImpl implements AppDAO {

    // define field for entity manager
    private final EntityManager entityManager;

    // inject entity manager using constructor injection
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
    public List<Course> findCoursesByInstructorID(int theID) {
        // create query
        TypedQuery<Course> query = entityManager.createQuery(
                "from Course where instructor.id = : data", Course.class);

        // Set NamedParameter
        query.setParameter("data",theID);

        // execute and return the query results
        return query.getResultList();

    }

    @Override
    public Instructor findInstructorByIdJoinFetch(int theID) {

        // EVEN WITH THE FetchType = LAZY ON OneToMany THE QUERY
        // STILL LOADS THE INSTRUCTOR WITH COURSES !!!

        // Step 1: Get CriteriaBuilder instance
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Step 2: Create CriteriaQuery for the Instructor entity
        CriteriaQuery<Instructor> criteriaQuery = criteriaBuilder.createQuery(Instructor.class);

        // Step 3: Define the root entity (Instructor) and specify joins
        Root<Instructor> instructorRoot = criteriaQuery.from(Instructor.class);
        instructorRoot.fetch("courses", JoinType.INNER);
        instructorRoot.fetch("instructorDetail", JoinType.INNER);

        // Step 4: Set the where condition
        criteriaQuery.select(instructorRoot)
                .where(criteriaBuilder.equal(instructorRoot.get("id"),
                        criteriaBuilder.parameter(Integer.class, "data")));

        // Step 5: Create the typed query and set the parameter
        TypedQuery<Instructor> instructorTypedQuery = entityManager.createQuery(criteriaQuery);
        instructorTypedQuery.setParameter("data", theID);

        return instructorTypedQuery.getSingleResult();
    }
}







