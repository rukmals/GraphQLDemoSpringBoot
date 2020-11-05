package com.graphql.graphqldemo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @GetMapping(value = "/")
    public String Hello(){
        return "Hello";
    }
}
