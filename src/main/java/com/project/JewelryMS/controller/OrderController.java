package com.project.JewelryMS.controller;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.CalculatePointsRequest;
import com.project.JewelryMS.model.OrderDetail.OrderDetailRequest;
import com.project.JewelryMS.model.OrderDetail.OrderPromotionRequest;
import com.project.JewelryMS.model.OrderDetail.OrderTotalRequest;
import com.project.JewelryMS.service.CustomerService;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.QRService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/order")
@SecurityRequirement(name = "api")
public class OrderController {
    @Autowired
    OrderHandlerService orderHandlerService;
    @Autowired
    EmailService emailService;
    @Autowired
    QRService qrService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    CustomerService customerService;

    @PostMapping("initialize")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity saleCreateOrder(@RequestBody CreateOrderWrapper order){
        orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());

        return ResponseEntity.ok("");
    }
    //Product Buy Section
    @PostMapping("initializePB")
    public ResponseEntity saleCreateBuyOrder(@RequestBody CreateOrderBuyWrapper order){
        orderHandlerService.handleCreateOrderBuyWithDetails(order.getOrderRequest(), order.getBuyDetailList());
        return ResponseEntity.ok("Create Successfully");
    }

    @PostMapping("append-productBuy")
    public ResponseEntity<Void> addOrderBuyDetail(@RequestParam Long orderId, @RequestParam Long productBuyId) {
        orderHandlerService.addOrderBuyDetail(orderId, productBuyId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get-all-orderBuy")
    public ResponseEntity<List<OrderBuyResponse>> getAllBuyOrders() {
        List<OrderBuyResponse> buyOrders = orderHandlerService.getAllBuyOrder();
        return ResponseEntity.ok(buyOrders);
    }

    @GetMapping("get-orderBuy/{id}")
    public ResponseEntity<List<ProductBuyResponse>> getProductBuyByOrderId(@PathVariable(name = "id") Long orderId) {
        List<ProductBuyResponse> productBuys = orderHandlerService.getProductBuyByOrderId(orderId);
        return ResponseEntity.ok(productBuys);
    }

    //Product Buy Section

    @PostMapping(value = "initialize-qr", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity<BufferedImage> saleCreateOrderQR(@RequestBody CreateOrderWrapper order) {
        // Generate QR code value
        Long orderID = orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());
        String value = orderID.toString();
        System.out.println(order.toString());
        // Create QR code image
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(qrService.createQR(value));


        // Pass email details and QR code image data to email service
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(order.getEmail());
        emailDetail.setSubject("Your Order QR Code");
        emailDetail.setMsgBody("Please find your order QR code attached.");

        emailService.sendMailWithEmbeddedImage(emailDetail,
                qrImage,
                orderHandlerService.generateEmailOrderTable(orderID));

        // Return the QR code image as the HTTP response
        return ResponseEntity.ok(qrImage);
    }
    @PutMapping("append-product")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity saleAppendProductToOrder(){

        return ResponseEntity.ok("");
    }

    @GetMapping("get-order/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity<List<ProductResponse>> cashierGetPendingOrder(@PathVariable(name = "id") Long id) {
        List<ProductResponse> productResponses = orderHandlerService.getProductByOrderId(id);
        System.out.println(id);
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("get-all-order")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<OrderResponse>> getAllOrderTest(){
        System.out.println("reached getAllOrder");
        return ResponseEntity.ok(orderHandlerService.getAllOrder());
    }
    @PutMapping("payment")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity cashierCompleteOrder(){


        return ResponseEntity.ok("");

    }

    @GetMapping("/SubTotal")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity<Float> calculateTotalAmount(@RequestBody OrderDetailRequest orderDetailRequest) {
        Float totalAmount = orderDetailService.calculateSubTotal(orderDetailRequest);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/DiscountProduct")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity<Float> calculateDiscount(@RequestBody OrderPromotionRequest orderPromotionRequest) {
        Float totalAmount = orderDetailService.calculateDiscountProduct(orderPromotionRequest);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/OrderDetailsTotal")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")

    public ResponseEntity<Float> calculateOrderDetailTotal(@RequestBody OrderTotalRequest orderTotalRequest) {
        Float totalAmount = orderDetailService.TotalOrderDetails(orderTotalRequest);
        return ResponseEntity.ok(totalAmount);
    }

    @PostMapping("/OrderTotal")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<TotalOrderResponse> calculateOrderTotal(@RequestBody List<TotalOrderRequest> totalOrderRequests) {
        TotalOrderResponse totalOrderResponse = orderDetailService.totalOrder(totalOrderRequests);
        return ResponseEntity.ok(totalOrderResponse);
    }




    @PostMapping("calculate-customer-points")
    public ResponseEntity<Integer> calculateAndUpdatePoints(@RequestBody CalculatePointsRequest request) {
        return ResponseEntity.ok(customerService.calculateAndUpdatePoints(request));
    }

    @PatchMapping("/cash-confirm")
    public ResponseEntity confirmCashPayment(@RequestBody ConfirmCashPaymentRequest request){
        boolean isUpdated = orderHandlerService.updateOrderStatusCash(request);

        if (isUpdated) {
            return ResponseEntity.ok("Đơn hàng thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đơn hàng thanh toán thất bại");
        }

    }

}
