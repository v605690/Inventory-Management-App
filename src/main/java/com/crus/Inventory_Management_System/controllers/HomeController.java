package com.crus.Inventory_Management_System.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewIndexPage() {
        return "index";
    }
}
