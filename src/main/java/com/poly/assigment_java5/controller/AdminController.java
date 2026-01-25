package com.poly.assigment_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
}