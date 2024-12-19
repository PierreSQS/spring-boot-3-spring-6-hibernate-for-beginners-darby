package com.luv2code.springdemo.mvc;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// introduced in Sec6_Chap193
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    private String firstName;

    @NotNull(message="is required")
    @Size(min=1, message="is required")
    private String lastName = "";

}
