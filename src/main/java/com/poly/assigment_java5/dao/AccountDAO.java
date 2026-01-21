package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountDAO extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsernameAndPassword(String username, String password);

    // Dùng để validate khi đăng ký
    boolean existsByEmail(String email);
}