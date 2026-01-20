package com.poly.assigment_java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
    @Id
    @Column(name = "Username", length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 50)
    private String fullname;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 255)
    private String photo = "default.png";

    private Boolean activated = true;

    private Boolean role = false; // false: USER, true: ADMIN

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<CartItem> cartItems;
}