package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping; // Dùng PostMapping cho rõ ràng

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryDAO dao;

    @RequestMapping("/index")
    public String index(Model model) {
        Category item = new Category();
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());

        // 2. Trả về đúng thư mục admin (giả sử file view tên là category.html)
        return "admin/category";
    }

    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Category item = dao.findById(id).orElse(new Category());
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        return "admin/category";
    }

    @PostMapping("/create")
    public String create(Category item) {
        dao.save(item);
        return "redirect:/admin/category/index";
    }

    @PostMapping("/update")
    public String update(Category item) {
        dao.save(item);
        return "redirect:/admin/category/edit/" + item.getId();
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/admin/category/index";
    }
}