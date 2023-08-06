package org.top.ordersmvcapp.service;

import org.top.ordersmvcapp.entity.OrderProduct;

import java.util.List;
import java.util.Optional;

public interface OrderProductService {
    OrderProduct create(OrderProduct orderProduct);
    Optional<OrderProduct> getById(Integer id);
    List<OrderProduct> getAll();
    OrderProduct update(OrderProduct orderProduct);
    List<OrderProduct> getAllByOrderId(Integer id);    // получить все заказы определенного клиента
    List<OrderProduct> getAllByProductId(Integer id);          // получить все заказы определенного товара
    void deleteById(Integer id);
    void deleteAllByOrderId(Integer id);
}
