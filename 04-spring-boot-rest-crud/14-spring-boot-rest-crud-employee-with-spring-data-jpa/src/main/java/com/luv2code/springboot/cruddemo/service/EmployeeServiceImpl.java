package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dao.EmployeeRepository;
import com.luv2code.springboot.cruddemo.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository empRepo;

    @Override
    public List<Employee> findAll() {
        return empRepo.findAll();
    }

    @Override
    public Employee findById(int employeeID) {
        return empRepo.findById(employeeID)
                .orElseThrow(() -> new RuntimeException("Employee with ID "+employeeID+" not found!"));
    }

    @Override
    public Employee save(Employee employee) {
        return empRepo.save(employee);
    }

    @Override
    public void deleteById(int employeeID) {
        empRepo.deleteById(employeeID);
    }
}






