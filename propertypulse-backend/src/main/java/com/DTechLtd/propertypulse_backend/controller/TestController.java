package com.DTechLtd.propertypulse_backend.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String testProtected() {
        return "You have accessed a protected route!";
    }
}
