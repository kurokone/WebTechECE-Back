package com.backskeleton.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello, world!";
    }

    // @GetMapping("/hello/:age")
    // public Number age() {
    //     return 20;
    // }
}
