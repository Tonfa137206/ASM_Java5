package com.poly.assigment_java5.dao;

import com.poly.assigment_java5.entity.OrderDetail;
import com.poly.assigment_java5.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
    List<OrderDetail> findByOrderId(Long orderId);
}