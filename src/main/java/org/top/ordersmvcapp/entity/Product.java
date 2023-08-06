package org.top.ordersmvcapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "product_t")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name_f", nullable = false)
    private String name;
    @Column(name = "article_f", nullable = false)
    private String article;
    @Column(name = "price_f", nullable = false)
    private double price;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<OrderProduct> orderProducts;

    @Override
    public String toString(){
        return id + " - " + name + " - " + article + " - " + price;
    }
}
