package com.luv2code.springboot.cruddemo.dao;

import com.luv2code.springboot.cruddemo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO {

    // define field for entity manager
    private final EntityManager entityManager;


    // set up constructor injection
    public EmployeeDAOJpaImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public List<Employee> findAll() {

        // create a query
        TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee", Employee.class);

        // execute query and get result list
        return theQuery.getResultList();
    }

    @Override
    public Employee findById(int employeeID) {
        return entityManager.find(Employee.class, employeeID);
    }

    // WE DON'T USE @Transactional AT DAO-LAYER BUT ON THE SERVICE LAYER
    @Override
    public Employee save(Employee employee) {
        // if empID = 0 then save else update
        return entityManager.merge(employee);
    }

    // WE DON'T USE @Transactional AT DAO-LAYER BUT ON THE SERVICE LAYER
    @Override
    public void deleteById(int employeeID) {
        // delete Employee with ID if exists
        Optional.of(findById(employeeID)).ifPresent(entityManager::remove);
    }

}











