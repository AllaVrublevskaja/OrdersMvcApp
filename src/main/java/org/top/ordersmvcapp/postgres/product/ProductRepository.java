package org.top.ordersmvcapp.postgres.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.top.ordersmvcapp.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
