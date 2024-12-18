package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class StudentController {

    @Value("${countries}")
    private List<String> countriesList;

    @Value("${languages}")
    private List<String> languagesList;

    // shows the Student form
    @GetMapping("/showStudentForm")
    public String showForm(Model theModel) {

        // create a student object for preloading the Student Form
        Student theStudent = new Student();

        // add student object to the model
        theModel.addAttribute("student", theStudent);

        // add the list of countries to the model
        theModel.addAttribute("countries", countriesList);

        // add the list of languages to the model
        theModel.addAttribute("languages", languagesList);

        return "student-form";
    }

    // shows the Student Confirmation Form
    // proceeds the Form View Data above
    @PostMapping("/processStudentForm")
    public String processForm(@ModelAttribute("student") Student theStudent) {

        // log the input data
        log.info("theStudent: {} {}", theStudent.getFirstName(), theStudent.getLastName());

        return "student-confirmation";
    }

}








