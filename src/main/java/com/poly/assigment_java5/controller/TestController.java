package com.poly.assigment_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    // Chạy thử file index.html cũ của bạn ông
    @GetMapping("/test/home")
    public String home() {
        return "user/index"; // Nó sẽ tìm file templates/user/index.html
    }

    // Chạy thử file register.html cũ
    @GetMapping("/test/register")
    public String register() {
        return "user/register"; // Nó sẽ tìm file templates/user/register.html
    }

    // Chạy thử file login.html cũ
    @GetMapping("/test/login")
    public String login() {
        return "user/login";
    }

    // Chạy thử file cart.html cũ
    @GetMapping("/test/cart")
    public String cart() {
        return "user/cart";
    }
}