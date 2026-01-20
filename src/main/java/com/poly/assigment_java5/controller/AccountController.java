package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.AccountDAO;
import com.poly.assigment_java5.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {

    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        // Nếu đã login rồi thì về trang chủ
        if (session.getAttribute("account") != null) {
            return "redirect:/";
        }
        // Lấy thông báo lỗi từ flash (nếu có)
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        Account account = accountDAO.findById(username).orElse(null);

        if (account != null && account.getPassword().equals(password)) {
            if (!account.getActivated()) {
                redirectAttributes.addFlashAttribute("error", "Tài khoản đã bị khóa!");
                return "redirect:/login";
            }
            session.setAttribute("account", account);
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("account", new Account());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Account account,
                           @RequestParam String confirmPassword,
                           RedirectAttributes redirectAttributes) {
        if (accountDAO.existsById(account.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/register";
        }
        if (!account.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/register";
        }

        account.setActivated(true);
        account.setRole(false);
        account.setPhoto("default.png");
        accountDAO.save(account);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Mời đăng nhập.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "user/profile";
    }
}