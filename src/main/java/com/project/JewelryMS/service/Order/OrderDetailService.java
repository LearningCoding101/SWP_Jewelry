package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public OrderDetail saveOrderDetail(OrderDetail order) {
        return orderDetailRepository.save(order);
    }

    public List<OrderDetail> getAllOrders() {
        return orderDetailRepository.findAll();
    }

    public OrderDetail getOrderById(Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
