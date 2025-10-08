package org.aadi.short_bulletin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Maps to src/main/resources/templates/index.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Maps to src/main/resources/templates/login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Maps to src/main/resources/templates/register.html
    }
}