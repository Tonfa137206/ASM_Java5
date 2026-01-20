package com.poly.assigment_java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Username")
    private Account account;

    @Temporal(TemporalType.DATE)
    private Date createDate = new Date();

    private String address;  // Địa chỉ giao hàng (từ form checkout)

    private Integer status = 0;  // 0: chờ xử lý, 1: đang giao, 2: hoàn thành, 3: hủy

    private Double total = 0.0;  // <-- THÊM field này (tổng tiền đơn hàng)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    // Nếu cần thêm ghi chú hoặc phương thức thanh toán, thêm ở đây
}