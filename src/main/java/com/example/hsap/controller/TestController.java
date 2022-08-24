package com.example.hsap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TestController {
    @GetMapping("test")
    public String hello() {
        return "hello world testing...";
    }
    @GetMapping("now")
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
