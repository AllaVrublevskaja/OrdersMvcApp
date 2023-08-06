package org.top.ordersmvcapp.postgres.orderproduct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.top.ordersmvcapp.entity.OrderProduct;
import org.top.ordersmvcapp.service.OrderProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbOrderProductService implements OrderProductService {
    private final OrderProductRepository orderProductRepository;

    @Override
    public OrderProduct create(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    @Override
    public Optional<OrderProduct> getById(Integer id) {
        return orderProductRepository.findById(id);
    }

    @Override
    public List<OrderProduct> getAll() { return orderProductRepository.findAll();}

    @Override
    public void deleteById(Integer id) { orderProductRepository.deleteById(id); }

    @Override
    public OrderProduct update(OrderProduct orderProduct) {
        if (orderProductRepository.findById(orderProduct.getId()).isEmpty()) {
            return null;
        }
        return orderProductRepository.save(orderProduct);
    }

    @Override
    public List<OrderProduct> getAllByOrderId(Integer id) {
        return orderProductRepository.findAllByOrderId(id);
    }

    @Override
    public List<OrderProduct> getAllByProductId(Integer id) {
        return orderProductRepository.findAllByProductId(id);
    }

    @Override
    public void deleteAllByOrderId(Integer id) {
        orderProductRepository.deleteAllByOrderId(id);
    }
}