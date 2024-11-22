package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService empServ;

    // expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAllEmployees() {
        return empServ.findAll();
    }

    @GetMapping("/employees/{empID}")
    public Employee findEmployeeByID( @PathVariable int empID) {
        return empServ.findById(empID);
    }

}








