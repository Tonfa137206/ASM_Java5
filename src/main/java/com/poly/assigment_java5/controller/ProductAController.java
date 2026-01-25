package com.poly.assigment_java5.controller; // 1. Sửa package cho đúng chính tả

import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.dao.ProductDAO;
import com.poly.assigment_java5.entity.Category;
import com.poly.assigment_java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product") // Mapping gốc
public class ProductAController {

    @Autowired ProductDAO productDAO;
    @Autowired CategoryDAO categoryDAO;

    // Đường dẫn tuyệt đối tới thư mục ảnh trong project
    // Lưu ý: Khi deploy thật cần cấu hình đường dẫn khác
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/images/products/";

    @RequestMapping("/index")
    public String index(Model model) {
        Product item = new Product();
        model.addAttribute("item", item);
        model.addAttribute("items", productDAO.findAll());

        // 2. Trả về đúng file trong thư mục templates/admin/
        return "admin/admin-product";
    }

    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Product item = productDAO.findById(id).orElse(new Product());
        model.addAttribute("item", item);
        model.addAttribute("items", productDAO.findAll());
        return "admin/admin-product";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Product item,
                         @RequestParam("imageFile") MultipartFile file) throws IOException {

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            item.setImage(fileName);
        } else {
            item.setImage("default.png"); // Đảm bảo file này có tồn tại
        }

        item.setCreateDate(new Date());
        productDAO.save(item);

        // 3. Redirect về đúng mapping /admin/product/index
        return "redirect:/admin/product/index";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Product item,
                         @RequestParam("imageFile") MultipartFile file) throws IOException {

        Product oldItem = productDAO.findById(item.getId()).orElse(null);

        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            item.setImage(fileName);
        } else {
            if (oldItem != null) item.setImage(oldItem.getImage());
        }

        if (oldItem != null) item.setCreateDate(oldItem.getCreateDate());

        productDAO.save(item);

        // Redirect về trang edit để người dùng thấy kết quả cập nhật
        return "redirect:/admin/product/edit/" + item.getId();
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        productDAO.deleteById(id);
        return "redirect:/admin/product/index";
    }

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryDAO.findAll();
    }
}