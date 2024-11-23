package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // IN CASE THE USER PASSES AN ID IN JSON, SET ID = 0
        // THIS FORCES A SAVE OF NEW ITEM INSTEAD OF AN UPDATE !
        if (employee.getId() != 0)
            employee.setId(0);

        return new ResponseEntity<>(empServ.save(employee), HttpStatus.CREATED);
    }

    @PutMapping("employees")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(empServ.update(employee), HttpStatus.OK);
    }

    @DeleteMapping("/employees/{employeeID}")
    public ResponseEntity<Void> deleteEmployeeByID(@PathVariable int employeeID) {
        empServ.deleteById(employeeID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}








