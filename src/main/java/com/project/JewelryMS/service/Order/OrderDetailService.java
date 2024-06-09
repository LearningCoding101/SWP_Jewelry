package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.OrderDetail.OrderDetailRequest;
import com.project.JewelryMS.model.OrderDetail.OrderTotalRequest;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductSellRepository productSellRepository;

    @Autowired
    private PromotionRepository promotionRepository;

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


    public Float calculateSubTotal(OrderDetailRequest orderDetailRequest) {
        float totalAmount = 0;
        Optional<ProductSell> productSellOptional = productSellRepository.findById(orderDetailRequest.getProductSell_ID());
        if(productSellOptional.isPresent()) {
            ProductSell productSell = productSellOptional.get();
            float productCost = productSell.getCost();
            int quantity = orderDetailRequest.getQuantity();
            totalAmount = productCost * quantity;
        }
        return totalAmount;
    }

    public Float calculateTotal(OrderTotalRequest orderTotalRequest){
        Promotion promotion = promotionRepository.findById(orderTotalRequest.getPromotionID()).orElseThrow(() -> new IllegalArgumentException("Promotion ID not found"));
        Integer discount = promotion.getDiscount();
        Float percentage = discount/100.0F;
        Float discountAmount = orderTotalRequest.getSubTotal() * percentage;
        Float total =  orderTotalRequest.getSubTotal() - discountAmount;  // Tổng giá trị sau khi áp dụng giảm giá
        return total;
    }
}
