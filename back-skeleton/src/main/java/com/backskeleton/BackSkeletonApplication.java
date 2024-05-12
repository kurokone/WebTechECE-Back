package com.backskeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.backskeleton.webconfig")
@ComponentScan("com.backskeleton.controllers")
@ComponentScan("com.backskeleton.services")
@ComponentScan("com.backskeleton.exception")
public class BackSkeletonApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackSkeletonApplication.class, args);
    }
}
