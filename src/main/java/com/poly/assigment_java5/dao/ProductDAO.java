package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Integer> {

    // 1. Tìm tất cả sản phẩm đang bán (Available = true)
    // Spring Data JPA sẽ tự động tạo câu lệnh SQL dựa trên tên hàm
    List<Product> findByAvailableTrue();

    // 2. Tìm sản phẩm theo mã loại (Category Id)
    // Query method chuẩn: tìm theo thuộc tính category.id
    List<Product> findByCategoryId(Integer cid);
}