package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Order.TotalOrderRequest;
import com.project.JewelryMS.model.Order.TotalOrderResponse;
import com.project.JewelryMS.model.OrderDetail.OrderDetailRequest;
import com.project.JewelryMS.model.OrderDetail.OrderPromotionRequest;
import com.project.JewelryMS.model.OrderDetail.OrderTotalRequest;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (productSellOptional.isPresent()) {
            ProductSell productSell = productSellOptional.get();
            float productCost = productSell.getCost();
            int quantity = orderDetailRequest.getQuantity();
            totalAmount = productCost * quantity;
        }
        return totalAmount;
    }

    public Float calculateDiscountProduct(OrderPromotionRequest orderPromotionRequest) {
        Promotion promotion = promotionRepository.findById(orderPromotionRequest.getPromotionID()).orElseThrow(() -> new IllegalArgumentException("Promotion ID not found"));
        Integer discount = promotion.getDiscount();
        Float percentage = discount / 100.0F;
        Float totalAmount = 0.0F;
        Optional<ProductSell> productSellOptional = productSellRepository.findById(orderPromotionRequest.getProductSell_ID());
        if (productSellOptional.isPresent()) {
            ProductSell productSell = productSellOptional.get();
            float productCost = productSell.getCost();
            int quantity = orderPromotionRequest.getQuantity();
            totalAmount = productCost * quantity;
        }
        return totalAmount * percentage;
    }

    public Float TotalOrderDetails(OrderTotalRequest orderTotalRequest) {
        Float subtotal = orderTotalRequest.getSubTotal();
        Float discountProuduct = orderTotalRequest.getDiscountProduct();
        Float total = subtotal - discountProuduct;
        return total;
    }

    public TotalOrderResponse totalOrder(List<TotalOrderRequest> totalOrderRequests) {
        Float subTotalResponse = 0.0F;
        Float discount_priceResponse = 0.0F;
        Float totalResponse = 0.0F;
        for (TotalOrderRequest request : totalOrderRequests) {
            // Fetch product details
            Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSell_ID());
            if (productSellOpt.isPresent()) {
                ProductSell productSell = productSellOpt.get();
                Float cost = productSell.getCost();
                Float subtotal = cost * request.getQuantity();
                subTotalResponse += subtotal;
                // Fetch promotion details if provided
                Float discountAmount = 0.0F;
                if (request.getPromotion_ID() != null) {
                    Optional<Promotion> promotionOptional = promotionRepository.findById(request.getPromotion_ID());
                    if (promotionOptional.isPresent()) {
                        Integer discount = promotionOptional.get().getDiscount();
                        Float percentage = discount / 100.0F;
                        discountAmount = subtotal * percentage;
                        discount_priceResponse +=discountAmount;
                    }
                }

                // Calculate the total after discount
                Float totalDetails = subtotal - discountAmount;
                totalResponse += totalDetails;
            } else {
                throw new IllegalArgumentException("ProductSell ID not found: " + request.getProductSell_ID());
            }
        }
        TotalOrderResponse totalOrderResponse = new TotalOrderResponse();
        totalOrderResponse.setSubTotal(subTotalResponse);
        totalOrderResponse.setDiscount_Price(discount_priceResponse);
        totalOrderResponse.setTotal(totalResponse);
        return totalOrderResponse;
    }
}

