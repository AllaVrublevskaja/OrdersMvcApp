package org.top.ordersmvcapp.postgres.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.top.ordersmvcapp.entity.Order;
import org.top.ordersmvcapp.service.OrderService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbOrderService implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order create(Order order) {
        // TODO: убедиться в правильности этого метода, при необходимости починить
        return orderRepository.save(order);
    }

    @Override
    public Iterable<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Iterable<Order> getAllByClientId(Integer id) {
        return orderRepository.findAllByClient_Id(id);
    }

    @Override
    public Optional<Order> getById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteAllByClientId(Integer clientId) {
        orderRepository.deleteAllByClientId(clientId);
    }

    @Override
    public Order update(Order order) {
        if (orderRepository.findById(order.getId()).isEmpty()) {
            return null;
        }
        return orderRepository.save(order);
    }
}

