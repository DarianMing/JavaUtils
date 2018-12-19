package com.lm.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private String firstName;
    private String lastName;
    private String job;
    private String gender;
    private int age;
    private int salary;
}
