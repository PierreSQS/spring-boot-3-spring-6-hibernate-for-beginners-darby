package com.luv2code.springboot.cruddemo.dao;

import com.luv2code.springboot.cruddemo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO {

    // define field for entity manager
    private final EntityManager entityManager;

    @Override
    public List<Employee> findAll() {

        // create a query
        TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee", Employee.class);

        // execute query and get the result list
        return theQuery.getResultList();
    }

    @Override
    public Employee findById(int theId) {

        // get employee
        return entityManager.find(Employee.class, theId);
    }

    @Override
    public Employee save(Employee theEmployee) {

        // save employee
        return entityManager.merge(theEmployee);
    }

    @Override
    public void deleteById(int theId) {

        // find employee by id
        Employee theEmployee = entityManager.find(Employee.class, theId);

        // remove employee
        entityManager.remove(theEmployee);
    }
}











