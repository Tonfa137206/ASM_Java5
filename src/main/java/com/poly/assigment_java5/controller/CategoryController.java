package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryController {

    @Autowired
    CategoryDAO dao;

    // 1. Hiển thị trang quản lý (Index)
    @RequestMapping("/admin/category/index")
    public String index(Model model) {
        Category item = new Category();
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        return "admin/category"; // Trả về view admin/category.html
    }

    // 2. Chức năng Edit (Lấy dữ liệu lên form)
    @GetMapping("/admin/category/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Category item = dao.findById(id).orElse(new Category());
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        return "admin/category";
    }

    // 3. Chức năng Create (Tạo mới)
    @PostMapping("/admin/category/create")
    public String create(Category item) {
        dao.save(item);
        return "redirect:/admin/category/index";
    }

    // 4. Chức năng Update (Cập nhật)
    @PostMapping("/admin/category/update")
    public String update(Category item) {
        dao.save(item);
        return "redirect:/admin/category/edit/" + item.getId();
    }

    // 5. Chức năng Delete (Xóa)
    @GetMapping("/admin/category/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/admin/category/index";
    }
}