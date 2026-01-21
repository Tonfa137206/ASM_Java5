package com.poly.assigment_java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
>>>>>>> e8c30aa804868be1a986f72976b8b1fac2582902
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

<<<<<<< HEAD
    @Column(name = "name", columnDefinition = "nvarchar(50) not null")
    private String name;
=======
    @Column(nullable = false, length = 50)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;
>>>>>>> e8c30aa804868be1a986f72976b8b1fac2582902
}