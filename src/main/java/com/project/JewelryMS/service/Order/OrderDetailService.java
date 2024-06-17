package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Order.TotalOrderRequest;
import com.project.JewelryMS.model.Order.TotalOrderResponse;
import com.project.JewelryMS.model.OrderDetail.*;
import com.project.JewelryMS.repository.GuaranteeRepository;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.security.Timestamp;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @Autowired
    private GuaranteeRepository guaranteeRepository;

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

    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findOrderDetailsByOrderId(orderId);
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

    public List<OrderDetailResponse> calculateAndSetGuaranteeEndDate(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        List<OrderDetailResponse> responses = orderDetails.stream()
                .filter(orderDetail -> orderDetail.getPurchaseOrder().getPK_OrderID().equals(orderId))
                .map(this::processOrderDetail)
                .toList();

        return responses;
    }

    private OrderDetailResponse processOrderDetail(OrderDetail orderDetail) {
        ProductSell productSell = orderDetail.getProductSell();
        if (productSell != null) {
            Optional<Guarantee> guaranteeOpt = guaranteeRepository.findByProductSell(productSell);

            if (guaranteeOpt.isPresent()) {
                Guarantee guarantee = guaranteeOpt.get();
                Integer warrantyPeriodMonth = guarantee.getWarrantyPeriodMonth();

                // Calculate guaranteeEndDate
                Timestamp now = new Timestamp(System.currentTimeMillis());
                Timestamp guaranteeEndDate = calculateGuaranteeEndDate(now, warrantyPeriodMonth);
                orderDetail.setGuaranteeEndDate(guaranteeEndDate);

                orderDetailRepository.save(orderDetail);

                // Create response
                return mapToOrderDetailResponse(orderDetail);
            }
        }
        return null;
    }
    private Timestamp calculateGuaranteeEndDate(Timestamp startDate, Integer warrantyPeriodMonth) {
        LocalDateTime startDateTime = startDate.toLocalDateTime();
        startDateTime = startDateTime.plusMonths(warrantyPeriodMonth);
        return Timestamp.valueOf(startDateTime);
    }

    private OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setPK_ODID(orderDetail.getPK_ODID());
        response.setProductSell_ID(orderDetail.getProductSell().getProductID());
        response.setPurchaseOrder_ID(orderDetail.getPurchaseOrder().getPK_OrderID());
        response.setQuantity(orderDetail.getQuantity());
        response.setGuaranteeEndDate(orderDetail.getGuaranteeEndDate());
        return response;
    }

}

