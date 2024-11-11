package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    public void saveInstructor(Instructor theInstructor) {
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
        Instructor foundInstructor = entityManager.find(Instructor.class, theId);

        // remove instructor from all the associated Course
        // Otherwise Constraint Violation
        foundInstructor.getCourses().forEach(course -> course.setInstructor(null));

        // delete the instructor
        entityManager.remove(foundInstructor);
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

        // EVEN WITH THE FetchType = LAZY ON OneToMany, THE QUERY
        // STILL LOADS THE INSTRUCTOR WITH COURSES !!!
        TypedQuery<Instructor> instructorTypedQuery = entityManager.createQuery(
                "select i from Instructor i " +
                        "JOIN FETCH i.courses " +
                        "JOIN FETCH i.instructorDetail " +
                        "where i.id = :data", Instructor.class);

        instructorTypedQuery.setParameter("data", theID);

        return instructorTypedQuery.getSingleResult();
    }

    @Override
    @Transactional // DON'T FORGET IT SINCE WE ARE CHANGING THE DB-DATA
    public void updateInstructor(Instructor instructorUpdate) {
        // MERGE WILL UPDATE ANY EXISTING INSTRUCTOR !!
        entityManager.merge(instructorUpdate);
    }

    @Override
    @Transactional // DON'T FORGET IT SINCE WE ARE CHANGING THE DB-DATA
    public void updateCourse(Course courseUpdate) {
        // MERGE WILL UPDATE ANY EXISTING COURSE !!
        entityManager.merge(courseUpdate);
    }

    @Override
    public Course findCourseByID(int theID) {
        return entityManager.find(Course.class, theID);
    }

    @Override
    @Transactional
    public void deleteCourseByID(int theID) {
        // find the course to remove
        Course foundCourse = findCourseByID(theID);

        // remove the associated Instructor
        foundCourse.setInstructor(null);

        // remove the course
        entityManager.remove(foundCourse);
    }

    @Override
    @Transactional
    public void saveCourse(Course course) {
        entityManager.persist(course);
    }

    @Override
    public Course findCourseAndReviewsByCourseId(int theId) {
        // create query
        TypedQuery<Course> courseTypedQuery = entityManager.createQuery(
                "select c from Course c " +
                        "JOIN FETCH c.reviews " +
                        "where c.id = :data", Course.class);

        // set Parameter
        courseTypedQuery.setParameter("data", theId);

        // execute query
        return courseTypedQuery.getSingleResult();

    }
}







