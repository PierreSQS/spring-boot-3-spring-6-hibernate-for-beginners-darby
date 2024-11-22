// New to Chap119
package com.luv2code.springboot.cruddemo.dao;

import com.luv2code.springboot.cruddemo.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();

    Employee findById(int employeeID);

    Employee save(Employee employee);

    void deleteById(int employeeID);

}
