package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    List<Product> findByAvailableTrue();
//    List<Product> findByApprovedTrue();  // Mới: cho index show chỉ approved
}