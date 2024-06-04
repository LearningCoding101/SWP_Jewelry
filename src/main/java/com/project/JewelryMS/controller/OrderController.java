package com.project.JewelryMS.controller;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.CalculatePointsRequest;
import com.project.JewelryMS.model.OrderDetail.OrderDetailRequest;
import com.project.JewelryMS.service.CustomerService;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.QRService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/order")
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
    public ResponseEntity saleCreateOrder(@RequestBody CreateOrderWrapper order){
        orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());

        return ResponseEntity.ok("");
    }
    @PostMapping(value = "initialize-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> saleCreateOrderQR(@RequestBody CreateOrderWrapper order) {
        // Generate QR code value
        Long orderID = orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());
        String value = orderID.toString();

        // Create QR code image
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(qrService.createQR(value));


        // Pass email details and QR code image data to email service
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("hahoang433322@gmail.com");
        emailDetail.setSubject("Your Order QR Code");
        emailDetail.setMsgBody("Please find your order QR code attached.");

        emailService.sendMailWithEmbeddedImage(emailDetail,
                qrImage,
                orderHandlerService.generateEmailOrderTable(orderID));

        // Return the QR code image as the HTTP response
        return ResponseEntity.ok(qrImage);
    }
    @PutMapping("append-product")
    public ResponseEntity saleAppendProductToOrder(){

        return ResponseEntity.ok("");
    }

    @GetMapping("get-order/{id}")
    public ResponseEntity<List<ProductResponse>> cashierGetPendingOrder(@PathVariable(name = "id") Long id) {
        List<ProductResponse> productResponses = orderHandlerService.getProductByOrderId(id);
        System.out.println(id);
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("get-all-order")
    public ResponseEntity<List<OrderResponse>> getAllOrderTest(){
        System.out.println("reached getAllOrder");
        return ResponseEntity.ok(orderHandlerService.getAllOrder());
    }
    @PutMapping("payment")
    public ResponseEntity cashierCompleteOrder(){


        return ResponseEntity.ok("");

    }

    @PostMapping("calculate-total")
    public ResponseEntity<Float> calculateTotalAmount(@RequestBody OrderDetailRequest orderDetailRequest) {
        List<OrderDetail> orderDetails = orderDetailRequest.getOrderDetails();
        Float totalAmount = orderDetailService.calculateTotalAmount(orderDetails);
        return ResponseEntity.ok(totalAmount);
    }


    @PostMapping("calculate-points")
    public void calculateAndUpdatePoints(@RequestBody CalculatePointsRequest request) {
        customerService.calculateAndUpdatePoints(request);
    }

}
