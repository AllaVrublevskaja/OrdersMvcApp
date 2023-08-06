package org.top.ordersmvcapp.postgres.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.top.ordersmvcapp.entity.Product;
import org.top.ordersmvcapp.service.ProductService;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class DbProductService implements ProductService {
    public final ProductRepository productRepository;

    @Override
    public Product register(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Iterable<Product> getAll() { return productRepository.findAll(); }

    @Override
    public void deleteById(Integer id) { productRepository.deleteById(id); }

    @Override
    public Product update(Product product) {
        if (productRepository.findById(product.getId()).isEmpty()) {
            return null;
        }
        return productRepository.save(product);
    }
}
