package org.top.ordersmvcapp.postgres.orderproduct;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.top.ordersmvcapp.entity.OrderProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends CrudRepository<OrderProduct, Integer> {
    Optional<OrderProduct> findByOrderId(Integer orderId);
    void deleteAllByOrderId(Integer orderId);
    List<OrderProduct> findAll();
    List<OrderProduct> findAllByOrderId(Integer id);
    List<OrderProduct> findAllByProductId(Integer id);
}
