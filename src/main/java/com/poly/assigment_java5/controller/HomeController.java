package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.ProductDAO;
import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.dao.CartItemDAO;
import com.poly.assigment_java5.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired private ProductDAO productDAO;
    @Autowired private CategoryDAO categoryDAO;
    @Autowired private CartItemDAO cartItemDAO;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("products", productDAO.findByAvailableTrue());  // ← sửa lại dòng này
        model.addAttribute("categories", categoryDAO.findAll());

        // Giữ nguyên phần tính cartCount
        Account account = (Account) session.getAttribute("account");
        int count = 0;
        if (account != null) {
            count = cartItemDAO.findByAccount(account).stream()
                    .mapToInt(item -> item.getQuantity()).sum();
        }
        model.addAttribute("cartCount", count);

        return "user/index";
    }
}