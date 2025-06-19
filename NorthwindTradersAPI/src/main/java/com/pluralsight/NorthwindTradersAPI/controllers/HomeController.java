package com.pluralsight.NorthwindTradersAPI.controllers;

import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String sayHello(@RequestParam(required = false) String country) {
        // If country is given, return "Hello <country>"
        if (country != null) {
            return "Hello " + country;
        }

        // If country is not given, return "Hello World"
        return "Hello World";
    }
}
