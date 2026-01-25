package com.poly.assigment_java5.controller;

import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.dao.ProductDAO;
import com.poly.assigment_java5.entity.Category;
import com.poly.assigment_java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    // Alternative home page mapping
    @RequestMapping("/home")
    public String homeAlternative(Model model) {
        return "redirect:/";
    }

    // Product List Page - All available products
    @RequestMapping("/product/list")
    public String list(Model model) {
        List<Product> products = productDAO.findByAvailableTrue();
        model.addAttribute("items", products);
        model.addAttribute("pageTitle", "Tất cả sản phẩm");
        return "user/product";
    }

    // Product Detail Page
    @RequestMapping("/product/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Product item = productDAO.findById(id).orElse(null);
        if (item == null) {
            // Redirect to product list if product not found
            return "redirect:/product/list";
        }
        model.addAttribute("item", item);
        
        // Add related products from same category
        if (item.getCategory() != null) {
            List<Product> relatedProducts = productDAO.findByCategoryId(item.getCategory().getId());
            // Remove current product from related products
            relatedProducts.removeIf(p -> p.getId().equals(id));
            model.addAttribute("relatedProducts", relatedProducts);
        }
        
        return "user/product-detail";
    }

    // Product List by Category - Filter by category
    @RequestMapping("/product/list-by-category/{cid}")
    public String listByCategory(Model model, @PathVariable("cid") Integer cid) {
        List<Product> products = productDAO.findByCategoryId(cid);
        model.addAttribute("items", products);
        
        // Add category info for page title
        Category category = categoryDAO.findById(cid).orElse(null);
        if (category != null) {
            model.addAttribute("pageTitle", "Danh mục: " + category.getName());
            model.addAttribute("currentCategory", category);
        } else {
            model.addAttribute("pageTitle", "Danh mục không tồn tại");
        }
        
        return "user/product";
    }

    // Search products (optional enhancement)
    @RequestMapping("/product/search")
    public String search(Model model, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // This would require a search method in ProductDAO
            // For now, return all products
            List<Product> products = productDAO.findByAvailableTrue();
            model.addAttribute("items", products);
            model.addAttribute("pageTitle", "Kết quả tìm kiếm: " + keyword);
            model.addAttribute("searchKeyword", keyword);
        } else {
            return "redirect:/product/list";
        }
        return "user/product";
    }

    // Test endpoint to verify data loading
    @RequestMapping("/test/data")
    public String testData(Model model) {
        List<Product> products = productDAO.findAll();
        List<Product> availableProducts = productDAO.findByAvailableTrue();
        List<Category> categories = categoryDAO.findAll();
        
        model.addAttribute("productCount", products.size());
        model.addAttribute("availableProductCount", availableProducts.size());
        model.addAttribute("categoryCount", categories.size());
        model.addAttribute("products", products);
        model.addAttribute("availableProducts", availableProducts);
        model.addAttribute("categories", categories);
        
        // Debug logging for test endpoint
        System.out.println("=== DEBUG TEST DATA ===");
        System.out.println("Total products: " + products.size());
        System.out.println("Available products: " + availableProducts.size());
        System.out.println("Categories: " + categories.size());
        
        if (!products.isEmpty()) {
            System.out.println("First product: " + products.get(0).getName() + " - Available: " + products.get(0).getAvailable());
        }
        
        return "test-data"; // This will show a simple test page
    }

    // Global Model Attribute - Categories for sidebar (available in ALL views)
    @ModelAttribute("cates")
    public List<Category> getCategories() {
        return categoryDAO.findAll();
    }

    // Global Model Attribute - Cart count (optional)
    @ModelAttribute("cartCount")
    public Integer getCartCount() {
        // This would integrate with cart service
        // For now, return a default value
        return 0;
    }
}