package com.poly.assigment_java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Cartitems")
public class CartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Username")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "Productid")
    private Product product;

    private Integer quantity = 1;

    @Transient
    public Double getAmount() {
        return product.getPrice() * quantity;
    }
}