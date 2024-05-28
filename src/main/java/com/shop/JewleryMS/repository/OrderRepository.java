package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDetail, Long> {
}
