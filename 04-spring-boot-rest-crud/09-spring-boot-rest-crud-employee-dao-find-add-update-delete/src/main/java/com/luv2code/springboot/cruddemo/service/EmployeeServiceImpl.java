package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dao.EmployeeDAO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO empDAO;

    @Override
    public List<Employee> findAll() {
        return empDAO.findAll();
    }

    @Override
    public Employee findById(int employeeID) {
        return empDAO.findById(employeeID);
    }

    @Override
    public Employee save(Employee employee) {
        return empDAO.save(employee);
    }

    @Override
    public void deleteById(int employeeID) {
        empDAO.deleteById(employeeID);
    }
}






