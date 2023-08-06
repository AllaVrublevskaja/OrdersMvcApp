package org.top.ordersmvcapp.postgres.order;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.top.ordersmvcapp.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    Iterable<Order> findAllByClient_Id(Integer id); // получить все заказы по id клиента
    void deleteAllByClientId(Integer clientId);
}
