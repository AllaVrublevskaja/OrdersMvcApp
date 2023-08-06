package org.top.ordersmvcapp.service;

import org.springframework.stereotype.Service;
import org.top.ordersmvcapp.entity.Order;

import java.util.Optional;

// интерфейс для выполнения операций с сущность Order
@Service
public interface OrderService {
    Order create(Order order);              // создать заказ
    Iterable<Order> getAll();              // получить все заказы
    Iterable<Order> getAllByClientId(Integer id);    // получить все заказы определенного клиента
    Optional<Order> getById(Integer id);             // получение заказа по id
    void deleteById(Integer id);// удалить заказ по id
    void deleteAllByClientId(Integer id);
    Order update(Order order);                // изменить заказ по id
}
