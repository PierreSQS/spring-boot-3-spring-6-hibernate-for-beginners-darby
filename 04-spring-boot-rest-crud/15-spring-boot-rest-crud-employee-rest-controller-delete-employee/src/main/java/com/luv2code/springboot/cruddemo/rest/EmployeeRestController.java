package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    public static final String EMPLOYEE_ID_NOT_FOUND = "Employee id not found";
    private final EmployeeService employeeService;

    // expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    // add mapping for GET /employees/{employeeId} - get employee by id

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {

        Employee theEmployee = employeeService.findById(employeeId);

        if (theEmployee == null) {
            throw new RuntimeException(EMPLOYEE_ID_NOT_FOUND + " - " + employeeId);
        }

        return theEmployee;
    }

    // add mapping for POST /employees - add new employee

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee) {

        // also just in case they pass an id in JSON ... set id to 0,
        // this is to force a save of new item ... instead of update

        theEmployee.setId(0);

        return employeeService.save(theEmployee);
    }

    // add mapping for PUT /employees - update existing employee

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {

        return employeeService.save(theEmployee);
    }

    // add mapping for PATCH /employees/{employeeId} - patch existing employee
    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId, @RequestBody Map<String, String> patchPayload) {
        Employee existingEmployee = employeeService.findById(employeeId);

        if (existingEmployee == null) {
            throw new RuntimeException(EMPLOYEE_ID_NOT_FOUND + " - " + employeeId);
        }

        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee id is not allowed to be updated");
        }

        Employee updatedEmployee = applyPatch(existingEmployee, patchPayload);

        return employeeService.save(updatedEmployee);
    }

    private Employee applyPatch(Employee existingEmployee, Map<String, String> patchPayload) {
        ObjectMapper mapper = new ObjectMapper();

        // convert existing employee to a JSON ObjectNode
        ObjectNode existingNode = mapper.convertValue(existingEmployee, ObjectNode.class);

        // convert patchPayload to JSON ObjectNode
        ObjectNode patchNode = mapper.convertValue(patchPayload, ObjectNode.class);

        // merge the patchNode to Employee existingNode
        existingNode.setAll(patchNode);

        // convert the Employee existingNode back to Employee
        return mapper.convertValue(existingNode, Employee.class);
    }

    // add mapping for DELETE /employees/{employeeId} - delete employee

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {

        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if null

        if (tempEmployee == null) {
            throw new RuntimeException(EMPLOYEE_ID_NOT_FOUND + " - " + employeeId);
        }

        employeeService.deleteById(employeeId);

        return "Deleted employee id - " + employeeId;
    }

}














