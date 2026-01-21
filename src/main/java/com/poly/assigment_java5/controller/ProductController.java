package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.*;
import com.poly.assigment_java5.entity.Account;
import com.poly.assigment_java5.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductDAO productDAO;
    @Autowired private CategoryDAO categoryDAO;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/products/";

    // Danh sách sản phẩm (cho mọi người)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productDAO.findByAvailableTrue());  // ← sửa lại dùng findByAvailableTrue()
        return "user/product";
    }

    // Chi tiết sản phẩm
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Product product = productDAO.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "user/product-detail";
    }

    // Thêm sản phẩm (bất kỳ user đã login nào cũng thêm được, hiện ngay)
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session, RedirectAttributes ra) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            ra.addFlashAttribute("error", "Vui lòng đăng nhập!");
            return "redirect:/login";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryDAO.findAll());
        return "user/product/add";  // templates/user/product/add.html (form thêm)
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Product product,
                      @RequestParam("imageFile") MultipartFile file,
                      HttpSession session, RedirectAttributes ra) throws IOException {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            ra.addFlashAttribute("error", "Vui lòng đăng nhập!");
            return "redirect:/login";
        }

        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), file.getBytes());
            product.setImage("/images/products/" + fileName);
        }
        product.setCreateDate(new Date());
        product.setAvailable(true);  // Mặc định có sẵn
        productDAO.save(product);
        ra.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        return "redirect:/products";
    }

    // Sửa sản phẩm (chỉ admin)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes ra) {
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getRole()) {
            ra.addFlashAttribute("error", "Chỉ admin mới có quyền!");
            return "redirect:/products";
        }
        Product product = productDAO.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryDAO.findAll());
        return "admin/product/edit";  // templates/admin/product/edit.html
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Product product,
                         @RequestParam(value = "imageFile", required = false) MultipartFile file,
                         HttpSession session, RedirectAttributes ra) throws IOException {
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getRole()) {
            ra.addFlashAttribute("error", "Chỉ admin mới có quyền!");
            return "redirect:/products";
        }
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), file.getBytes());
            product.setImage("/images/products/" + fileName);
        }
        productDAO.save(product);
        ra.addFlashAttribute("success", "Cập nhật thành công!");
        return "redirect:/products";
    }

    // Xóa sản phẩm (chỉ admin)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getRole()) {
            ra.addFlashAttribute("error", "Chỉ admin mới có quyền!");
            return "redirect:/products";
        }
        productDAO.deleteById(id);
        ra.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/products";
    }
}