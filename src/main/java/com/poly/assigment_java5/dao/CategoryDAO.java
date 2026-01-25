package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
    // JpaRepository đã có sẵn các hàm: findAll, findById, save, deleteById...
    // Không cần viết thêm gì trừ khi muốn tìm kiếm nâng cao
}