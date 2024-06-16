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
//@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
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


    // Create a new order
    @PostMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Void> saleCreateOrder(@RequestBody CreateOrderWrapper order) {
        orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());
        return ResponseEntity.ok().build();
    }

    // Create a new order and generate QR code
    @PostMapping(value = "/initialize-qr", produces = MediaType.IMAGE_PNG_VALUE)
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<BufferedImage> saleCreateOrderQR(@RequestBody CreateOrderWrapper order) {
        Long orderID = orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());
        String value = orderID.toString();
        System.out.println(value);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(qrService.createQR(value));

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(order.getEmail());
        emailDetail.setSubject("Your Order QR Code");
        emailDetail.setMsgBody("Please find your order QR code attached.");

        emailService.sendMailWithEmbeddedImage(emailDetail, qrImage, orderHandlerService.generateEmailOrderTable(orderID));

        return ResponseEntity.ok(qrImage);
    }

    // Append a product to an existing order
    @PutMapping("/append-product")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Void> saleAppendProductToOrder() {
        return ResponseEntity.ok().build();
    }

    // Get a pending order by ID
    @GetMapping("/{id}")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<ProductResponse>> cashierGetPendingOrder(@PathVariable Long id) {
        List<ProductResponse> productResponses = orderHandlerService.getProductByOrderId(id);
        return ResponseEntity.ok(productResponses);
    }

    // Get all orders
    @GetMapping
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderHandlerService.getAllOrder());
    }

    // Complete order payment
    @PutMapping("/payment")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Void> cashierCompleteOrder() {
        return ResponseEntity.ok().build();
    }

    // Calculate order subtotal
    @PostMapping("/subtotal")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Float> calculateTotalAmount(@RequestBody OrderDetailRequest orderDetailRequest) {
        Float totalAmount = orderDetailService.calculateSubTotal(orderDetailRequest);
        return ResponseEntity.ok(totalAmount);
    }

    // Calculate product discount
    @PostMapping("/discount")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Float> calculateDiscount(@RequestBody OrderPromotionRequest orderPromotionRequest) {
        Float totalAmount = orderDetailService.calculateDiscountProduct(orderPromotionRequest);
        return ResponseEntity.ok(totalAmount);
    }

    // Calculate order details total
    @PostMapping("/order-details-total")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Float> calculateOrderDetailTotal(@RequestBody OrderTotalRequest orderTotalRequest) {
        Float totalAmount = orderDetailService.TotalOrderDetails(orderTotalRequest);
        return ResponseEntity.ok(totalAmount);
    }

    // Calculate order total
    @PostMapping("/total")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<TotalOrderResponse> calculateOrderTotal(@RequestBody List<TotalOrderRequest> totalOrderRequests) {
        TotalOrderResponse totalOrderResponse = orderDetailService.totalOrder(totalOrderRequests);
        return ResponseEntity.ok(totalOrderResponse);
    }

    // Calculate and update customer points
    @PostMapping("/calculate-customer-points")
    public ResponseEntity<Integer> calculateAndUpdatePoints(@RequestBody CalculatePointsRequest request) {
        return ResponseEntity.ok(customerService.calculateAndUpdatePoints(request));
    }

    // Confirm cash payment
    @PatchMapping("/cash-confirm")
    public ResponseEntity<String> confirmCashPayment(@RequestBody ConfirmCashPaymentRequest request) {
        boolean isUpdated = orderHandlerService.updateOrderStatusCash(request);
        if (isUpdated) {
            return ResponseEntity.ok("Cash payment successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cash payment failed");
        }
    }
}
