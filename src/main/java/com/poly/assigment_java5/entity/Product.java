package com.poly.assigment_java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String image;

    private Double price;

    @Temporal(TemporalType.DATE)
    private Date createDate = new Date();

    private Boolean available = true;

//    private Boolean approved = false;  // Mới: default false, admin duyệt thì true

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Category category;

    @Column(length = 500)
    private String description;

    private Integer stock = 100;

    @Column(length = 50)
    private String origin;
}