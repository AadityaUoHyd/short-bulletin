package org.aadi.short_bulletin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @GetMapping("/bulletins")
    public String getUserBulletins(Model model) {
        // Add model attributes, e.g., list of dates
        return "user-bulletin";
    }

    @GetMapping("/bulletin/{date}")
    public String getBulletinDetail(Model model) {
        // Add bulletin details
        return "bulletin-detail";
    }
}