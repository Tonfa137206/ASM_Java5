package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.CartItem;
import com.poly.assigment_java5.entity.Account;
import com.poly.assigment_java5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemDAO extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByAccount(Account account);

    Optional<CartItem> findByAccountAndProduct(Account account, Product product);

    @Transactional
    void deleteByAccount(Account account);

    int countByAccount(Account account);
}