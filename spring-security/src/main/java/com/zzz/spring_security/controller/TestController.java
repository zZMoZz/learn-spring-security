package com.zzz.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test1")
    public String test1() {
        return "Test 1 Page";
    }

    @GetMapping("/test2")
    public String test2() {
        return "Test 2 Page";
    }

    @GetMapping("/test3")
    public String test3() {
        return "Test 3 Page";
    }

    @GetMapping("/test4")
    public String test4() {
        return "Test 4 Page";
    }

    @GetMapping("/test5")
    public String test5() {
        return "Test 5 Page";
    }
}
