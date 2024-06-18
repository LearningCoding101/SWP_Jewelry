package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.OrderDetailResponse;
import com.project.JewelryMS.repository.ProductBuyRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.ProductBuyService;
import com.project.JewelryMS.service.ProductSellService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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
    @Autowired
    ProductBuyRepository productBuyRepository;
    @Autowired
    ProductBuyService productBuyService;
    @Autowired
    OrderBuyDetailService orderBuyDetailService;
    @Autowired
    EmailService emailService;
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

    public Long handleCreateOrderWithDetails(CreateOrderRequest orderRequest, List<CreateOrderDetailRequest> detailRequest, String email){
        PurchaseOrder order = new PurchaseOrder();
        Long id = -1L;
        order.setStatus(orderRequest.getStatus());
        order.setPurchaseDate(new Date());
        order.setPaymentType(orderRequest.getPaymentType());
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setEmail(email);
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
    //Product Buy Section///////////////////////////////////////////////////////////////////////////////////////////////
    @Transactional
    public Long createOrderWithBuyDetails(PurchaseOrder purchaseOrder, List<OrderBuyDetail> list){
        Set<OrderBuyDetail> detailSet = new HashSet<>();
        for(OrderBuyDetail detail : list){
            detail.setPurchaseOrder(purchaseOrder);
            detailSet.add(detail);
        }
        purchaseOrder.setOrderBuyDetails(detailSet);
        orderService.saveOrder(purchaseOrder);
        return purchaseOrder.getPK_OrderID();
    }

    public Long handleCreateOrderBuyWithDetails(CreateOrderRequest orderRequest, List<CreateOrderBuyDetailRequest> detailRequest){
        PurchaseOrder order = new PurchaseOrder();
        Long id = -1L;
        order.setStatus(orderRequest.getStatus());
        order.setPurchaseDate(new Date());
        order.setPaymentType(orderRequest.getPaymentType());
        order.setTotalAmount(orderRequest.getTotalAmount());
        List<OrderBuyDetail> orderBuyDetails = new ArrayList<>();
        for(CreateOrderBuyDetailRequest detail : detailRequest){
            OrderBuyDetail orderBuyDetail = new OrderBuyDetail();
            orderBuyDetail.setProductBuy(productBuyService.getProductBuyById2(detail.getProductBuyID()));
            orderBuyDetail.setPurchaseOrder(order);
            orderBuyDetails.add(orderBuyDetail);
        }

        if(!orderBuyDetails.isEmpty()){
            id = createOrderWithBuyDetails(order, orderBuyDetails);
        }
        return id;
    }
    public void addOrderBuyDetail(Long orderId, Long productBuyId) {
        PurchaseOrder order = orderService.getOrderById(orderId);
        ProductBuy productBuy= productBuyService.getProductBuyById2(productBuyId);

        OrderBuyDetail orderBuyDetail = new OrderBuyDetail();
        orderBuyDetail.setProductBuy(productBuy);
        orderBuyDetail.setPurchaseOrder(order);
        orderBuyDetailService.saveOrderBuyDetail(orderBuyDetail);
    }

    public List<OrderBuyResponse> getAllBuyOrder(){
        List<OrderBuyResponse> result = new ArrayList<>();
        List<PurchaseOrder> orderList = orderService.getAllOrders();
        System.out.println(orderList.toString());
        for(PurchaseOrder order : orderList) {
            if (order.getOrderBuyDetails() != null && !order.getOrderBuyDetails().isEmpty()) {
                OrderBuyResponse orderToGet = new OrderBuyResponse();
                orderToGet.setStatus(order.getStatus());
                orderToGet.setPaymentType(order.getPaymentType());
                orderToGet.setTotalAmount(order.getTotalAmount());
                orderToGet.setPurchaseDate(order.getPurchaseDate());
                Set<ProductBuyResponse> productBuyResponses = new HashSet<>();
                List<OrderBuyDetail> iterateList = order.getOrderBuyDetails().stream().toList();
                for (OrderBuyDetail item : iterateList) {
                    ProductBuy product = item.getProductBuy();
                    ProductBuyResponse response = new ProductBuyResponse();
                    response.setProductBuyID(product.getPK_ProductBuyID());
                    response.setCategoryName(product.getCategory().getName());
                    response.setMetalType(product.getMetalType());
                    response.setGemstoneType(product.getGemstoneType());
                    response.setPbName(product.getPbName());
                    response.setCost(product.getPbCost());
                    productBuyResponses.add(response);
                }
                orderToGet.setProductBuyDetail(productBuyResponses);
                result.add(orderToGet);
            }
        }
        return result;
    }

    public List<ProductBuyResponse> getProductBuyByOrderId(Long orderID) {
        PurchaseOrder order = orderService.getOrderById(orderID);
        Set<ProductBuyResponse> productBuyResponses = new HashSet<>();
        System.out.println("reached " + order.getPurchaseDate());
        if (order.getOrderBuyDetails() != null && !order.getOrderBuyDetails().isEmpty()) {
            for (OrderBuyDetail item : order.getOrderBuyDetails()) {

                ProductBuy product = item.getProductBuy();
                ProductBuyResponse response = new ProductBuyResponse();
                response.setProductBuyID(product.getPK_ProductBuyID());
                response.setCategoryName(product.getCategory().getName());
                response.setMetalType(product.getMetalType());
                response.setGemstoneType(product.getGemstoneType());
                response.setPbName(product.getPbName());
                response.setCost(product.getPbCost());
                productBuyResponses.add(response);
            }
        }
        return new ArrayList<>(productBuyResponses);
    }
    //Product Buy Section///////////////////////////////////////////////////////////////////////////////////////////////

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
                response.setManufactureCost(product.getManufactureCost());
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
            response.setManufactureCost(product.getManufactureCost());
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
            response.setManufactureCost(product.getManufactureCost());
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
        int orderID = Integer.parseInt(info.replace("Thanh toan ", "").trim());

        PurchaseOrder orderToUpdate = orderService.getOrderById((long) orderID);
        System.out.println(orderToUpdate.toString());
        orderToUpdate.setStatus(3);
        calculateAndSetGuaranteeEndDate((long) orderID);
        sendConfirmationEmail((long) orderID, orderToUpdate.getEmail());
        System.out.println(orderToUpdate.toString());
        orderService.saveOrder(orderToUpdate);


    }
    public void sendConfirmationEmail(Long orderId, String recipientEmail) {
        // Prepare EmailDetail object
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(recipientEmail);
        emailDetail.setSubject("Confirmation Email for Order #" + orderId);
        emailDetail.setMsgBody("This is to confirm your order with ID #" + orderId + ". Thank you for your purchase.");

        // Call the service method to send confirmation email
        emailService.sendConfirmEmail(orderId, emailDetail);
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
                calculateAndSetGuaranteeEndDate((long) request.getOrderID());
                System.out.println(orderToUpdate.toString());
                sendConfirmationEmail((long) request.getOrderID(), orderToUpdate.getEmail());
                return orderService.saveOrder(orderToUpdate) != null;
            } else{
                return false;
            }
        }

    }


    //Thai Dang fix may thang order detail bo len day, t lamf wrapper tam thoi thoi
    public List<OrderDetailResponse> calculateAndSetGuaranteeEndDate(Long orderID){
        return orderDetailService.calculateAndSetGuaranteeEndDate(orderID);
    }

}
