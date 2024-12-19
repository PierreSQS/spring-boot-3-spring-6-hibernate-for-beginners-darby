package com.luv2code.springboot.thymeleafdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    private String firstName;
    private String lastName;
    private String country;
    private String favoriteLanguage;
    // introduced in Sec6_Chap188
    private List<String> favoriteSystems;

}
