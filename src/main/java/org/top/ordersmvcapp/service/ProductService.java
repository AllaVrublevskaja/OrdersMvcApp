package org.top.ordersmvcapp.service;

import org.springframework.stereotype.Service;
import org.top.ordersmvcapp.entity.Product;

import java.util.Optional;

@Service
public interface ProductService {
    Product register(Product product);
    Optional<Product> getById(Integer id);
    Iterable<Product> getAll();
    void deleteById(Integer id);
    Product update(Product product);
}
