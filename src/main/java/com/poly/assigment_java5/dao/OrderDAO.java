package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.Order;
import com.poly.assigment_java5.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Order, Long> {
    List<Order> findByAccount(Account account);

    // Lấy lịch sử mua hàng của User (mới nhất lên đầu)
    List<Order> findByAccountUsernameOrderByCreateDateDesc(String username);

    // Thống kê đơn hàng theo trạng thái (Cho Admin)
    List<Order> findByStatus(Integer status);
}