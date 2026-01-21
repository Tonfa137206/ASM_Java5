package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
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
=======
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryDAO.findAll());
        return "category/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryDAO.save(category);
        redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Category category = categoryDAO.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        return "category/edit";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryDAO.save(category);
        redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        categoryDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        return "redirect:/categories";
>>>>>>> e8c30aa804868be1a986f72976b8b1fac2582902
    }
}