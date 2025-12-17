//package com.crus.Inventory_Management_System.controllers;
//
//import com.crus.Inventory_Management_System.entity.User;
//import com.crus.Inventory_Management_System.services.UserService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public String registrationForm(@Valid @ModelAttribute("user") User user,
//                                   BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//            System.out.println("Validation errors found");
//            bindingResult.getAllErrors().forEach(error ->
//                    System.out.println("Validation error: " + error.getDefaultMessage()));
//            return "register";
//        }
//        try {
//            userService.registerUser(user);
//            System.out.println("User registered successfully");
//            return "redirect:/login";
//        } catch (IllegalStateException e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "register";
//        } catch (Exception e) {
//            model.addAttribute("errormessage", "Registration failed: " + e.getMessage());
//            return "register";
//        }
//    }
//
//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("user", new User());
//        return "register";
//    }
//}
