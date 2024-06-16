package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.PurchaseOrder;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.ProductSellService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderHandlerService {
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductSellService productSellService;
    @Autowired
    ProductSellRepository productSellRepository;
    @Transactional
    public Long createOrderWithDetails(PurchaseOrder purchaseOrder, List<OrderDetail> list){
        Set<OrderDetail> detailSet = new HashSet<>();
        for(OrderDetail detail : list){
            detail.setPurchaseOrder(purchaseOrder);
            detailSet.add(detail);
        }
        purchaseOrder.setOrderDetails(detailSet);
        orderService.saveOrder(purchaseOrder);
        return purchaseOrder.getPK_OrderID();
    }

    public Long handleCreateOrderWithDetails(CreateOrderRequest orderRequest, List<CreateOrderDetailRequest> detailRequest){
        PurchaseOrder order = new PurchaseOrder();
        Long id = -1L;
        order.setStatus(orderRequest.getStatus());
        order.setPurchaseDate(orderRequest.getPurchaseDate());
        order.setPaymentType(orderRequest.getPaymentType());
        order.setTotalAmount(orderRequest.getTotalAmount());
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CreateOrderDetailRequest detail : detailRequest){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setProductSell(productSellService.getProductSellById(detail.getProductID()));
            orderDetail.setPurchaseOrder(order);
            orderDetails.add(orderDetail);
        }

        if(!orderDetails.isEmpty()){
            id = createOrderWithDetails(order, orderDetails);
        }
        return id;
    }



    public void addOrderDetail(Long orderId, Long productId, Integer quantity) {
        PurchaseOrder order = orderService.getOrderById(orderId);
        ProductSell productSell = productSellService.getProductSellById(productId);

        OrderDetail orderDetailId = new OrderDetail();
        orderDetailId.setProductSell(productSell);
        orderDetailId.setPurchaseOrder(order);
        orderDetailId.setQuantity(quantity);
        orderDetailService.saveOrderDetail(orderDetailId);
    }
    public List<OrderResponse> getAllOrder(){
        List<OrderResponse> result = new ArrayList<>();
        List<PurchaseOrder> orderList = orderService.getAllOrders();
        System.out.println(orderList.toString());
        for(PurchaseOrder order : orderList){
            OrderResponse orderToGet = new OrderResponse();
            orderToGet.setStatus(order.getStatus());
            orderToGet.setPaymentType(order.getPaymentType());
            orderToGet.setTotalAmount(order.getTotalAmount());
            orderToGet.setPurchaseDate(order.getPurchaseDate());
            Set<ProductResponse> productResponses = new HashSet<>();
            List<OrderDetail> iterateList = order.getOrderDetails().stream().toList();
            for(OrderDetail item : iterateList){
                ProductSell product = item.getProductSell();
                ProductResponse response = new ProductResponse();
                response.setQuantity(item.getQuantity());
                response.setProductID(product.getProductID());
                response.setName(product.getPName());
                response.setCarat(product.getCarat());
                response.setChi(product.getChi());
                response.setCost(product.getCost());
                response.setDescription(product.getPDescription());
                response.setGemstoneType(product.getGemstoneType());
                response.setImage(product.getImage());
                response.setManufacturer(product.getManufacturer());
                response.setStatus(product.isPStatus());
                response.setCategory_id(product.getProductID());
                List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId((product.getProductID()));
                List<String> promotionIds = new ArrayList<>();

                for (Long promotionId : listPromotion) {
                    promotionIds.add(String.valueOf(promotionId));
                }

                response.setPromotion_id(promotionIds);


                productResponses.add(response);
            }
            orderToGet.setProductDetail(productResponses);
            result.add(orderToGet);

        }
        return result;
    }
    public List<ProductResponse> getProductByOrderId(Long orderID) {
        PurchaseOrder order = orderService.getOrderById(orderID);
        Set<ProductResponse> productResponses = new HashSet<>();
        System.out.println("reached " + order.getPurchaseDate());

        for (OrderDetail item : order.getOrderDetails()) {

            ProductSell product = item.getProductSell();
            ProductResponse response = new ProductResponse();

            response.setQuantity(item.getQuantity());
            response.setProductID(product.getProductID());
            response.setName(product.getPName());
            response.setCarat(product.getCarat());
            response.setChi(product.getChi());
            response.setCost(product.getCost());
            response.setDescription(product.getPDescription());
            response.setGemstoneType(product.getGemstoneType());
            response.setImage(product.getImage());
            response.setManufacturer(product.getManufacturer());
            response.setStatus(product.isPStatus());
            response.setCategory_id(product.getProductID());

            List<String> promotionIds = productSellRepository.findPromotionIdsByProductSellId(product.getProductID())
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            response.setPromotion_id(promotionIds);

            productResponses.add(response);
        }

        return new ArrayList<>(productResponses);
    }

    public List<ProductResponse> generateEmailOrderTable(Long orderID){
        PurchaseOrder order = orderService.getOrderById(orderID);
        OrderResponse orderToGet = new OrderResponse();
        orderToGet.setStatus(order.getStatus());
        orderToGet.setPaymentType(order.getPaymentType());
        orderToGet.setTotalAmount(order.getTotalAmount());
        orderToGet.setPurchaseDate(order.getPurchaseDate());
        Set<ProductResponse> productResponses = new HashSet<>();
        List<OrderDetail> iterateList = order.getOrderDetails().stream().toList();
        for(OrderDetail item : iterateList){
            ProductSell product = item.getProductSell();
            ProductResponse response = new ProductResponse();
            response.setQuantity(item.getQuantity());
            response.setProductID(product.getProductID());
            response.setName(product.getPName());
            response.setCarat(product.getCarat());
            response.setChi(product.getChi());
            response.setCost(product.getCost());


            response.setDescription(product.getPDescription());
            response.setGemstoneType(product.getGemstoneType());
            response.setImage(product.getImage());
            response.setManufacturer(product.getManufacturer());
            response.setStatus(product.isPStatus());
            response.setCategory_id(product.getProductID());
            List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId((product.getProductID()));
            List<String> promotionIds = new ArrayList<>();

            for (Long promotionId : listPromotion) {
                promotionIds.add(String.valueOf(promotionId));
            }

            response.setPromotion_id(promotionIds);


            productResponses.add(response);
        }
        return productResponses.stream().toList();

    }

    public void updateOrderStatus(String info){
        int orderID = Integer.parseInt(info.replace("Thanh-toan-", "").trim());

        PurchaseOrder orderToUpdate = orderService.getOrderById((long) orderID);
        System.out.println(orderToUpdate.toString());
        orderToUpdate.setStatus(3);
        System.out.println(orderToUpdate.toString());

        orderService.saveOrder(orderToUpdate);


    }

    public boolean updateOrderStatusCash(ConfirmCashPaymentRequest request){
        float paidAmount = request.getAmount();
        float askPrice = request.getTotal();
        if(paidAmount < askPrice){
            return false;
        } else {
            PurchaseOrder orderToUpdate = orderService.getOrderById(request.getOrderID());
            if(orderToUpdate != null){
                System.out.println(orderToUpdate.toString());
                orderToUpdate.setStatus(3);
                System.out.println(orderToUpdate.toString());
                return orderService.saveOrder(orderToUpdate) != null;
            } else{
                return false;
            }
        }

    }


}
