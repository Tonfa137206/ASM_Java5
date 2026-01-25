package com.poly.assigment_java5.config;

import com.poly.assigment_java5.dao.CategoryDAO;
import com.poly.assigment_java5.dao.ProductDAO;
import com.poly.assigment_java5.entity.Category;
import com.poly.assigment_java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private ProductDAO productDAO;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data to clean up input errors
        System.out.println("Clearing existing data...");
        productDAO.deleteAll();
        categoryDAO.deleteAll();

        // Insert Categories
        System.out.println("Creating categories...");
        Category category1 = new Category();
        category1.setName("Trái cây nhập khẩu");
        category1 = categoryDAO.save(category1);

        Category category2 = new Category();
        category2.setName("Rau củ hữu cơ");
        category2 = categoryDAO.save(category2);

        Category category3 = new Category();
        category3.setName("Nước ép");
        category3 = categoryDAO.save(category3);

        // Insert Products - 2 for each category
        System.out.println("Creating products...");

        // Products for "Trái cây nhập khẩu"
        Product product1 = new Product();
        product1.setName("Táo Envy");
        product1.setPrice(180000.0);
        product1.setImage("default.png");
        product1.setAvailable(true);
        product1.setCreateDate(new Date());
        product1.setCategory(category1);
        product1.setDescription("Táo Envy nhập khẩu từ New Zealand, giòn ngọt tự nhiên");
        product1.setStock(50);
        product1.setOrigin("New Zealand");
        productDAO.save(product1);

        Product product2 = new Product();
        product2.setName("Dâu tây Nhật");
        product2.setPrice(250000.0);
        product2.setImage("default.png");
        product2.setAvailable(true);
        product2.setCreateDate(new Date());
        product2.setCategory(category1);
        product2.setDescription("Dâu tây Nhật Bản cao cấp, ngọt thanh mát lành");
        product2.setStock(30);
        product2.setOrigin("Nhật Bản");
        productDAO.save(product2);

        // Products for "Rau củ hữu cơ"
        Product product3 = new Product();
        product3.setName("Cà rốt");
        product3.setPrice(25000.0);
        product3.setImage("default.png");
        product3.setAvailable(true);
        product3.setCreateDate(new Date());
        product3.setCategory(category2);
        product3.setDescription("Cà rốt hữu cơ Đà Lạt, giàu vitamin A");
        product3.setStock(100);
        product3.setOrigin("Đà Lạt");
        productDAO.save(product3);

        Product product4 = new Product();
        product4.setName("Rau cải xanh");
        product4.setPrice(15000.0);
        product4.setImage("default.png");
        product4.setAvailable(true);
        product4.setCreateDate(new Date());
        product4.setCategory(category2);
        product4.setDescription("Rau cải xanh hữu cơ tươi ngon, không thuốc trừ sâu");
        product4.setStock(80);
        product4.setOrigin("Việt Nam");
        productDAO.save(product4);

        // Products for "Nước ép"
        Product product5 = new Product();
        product5.setName("Nước cam");
        product5.setPrice(35000.0);
        product5.setImage("default.png");
        product5.setAvailable(true);
        product5.setCreateDate(new Date());
        product5.setCategory(category3);
        product5.setDescription("Nước cam tươi 100% không đường, giàu vitamin C");
        product5.setStock(60);
        product5.setOrigin("Việt Nam");
        productDAO.save(product5);

        Product product6 = new Product();
        product6.setName("Nước ép táo");
        product6.setPrice(40000.0);
        product6.setImage("default.png");
        product6.setAvailable(true);
        product6.setCreateDate(new Date());
        product6.setCategory(category3);
        product6.setDescription("Nước ép táo tự nhiên, không chất bảo quản");
        product6.setStock(45);
        product6.setOrigin("Việt Nam");
        productDAO.save(product6);

        System.out.println("Database populated successfully!");
        System.out.println("Created " + categoryDAO.count() + " categories");
        System.out.println("Created " + productDAO.count() + " products");
    }
}