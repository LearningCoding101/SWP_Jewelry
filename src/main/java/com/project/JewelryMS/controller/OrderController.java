package com.project.JewelryMS.controller;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.JewelryMS.model.Order.CreateOrderDetailRequest;
import com.project.JewelryMS.model.Order.CreateOrderRequest;
import com.project.JewelryMS.model.Order.CreateOrderWrapper;
import com.project.JewelryMS.model.Order.OrderResponse;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.QRService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    OrderHandlerService orderHandlerService;
    @Autowired
    QRService qrService;
    @PostMapping("initialize")
    public ResponseEntity saleCreateOrder(@RequestBody CreateOrderWrapper order){
        orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList());

        return ResponseEntity.ok("");
    }
    @PostMapping(value = "initialize-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> saleCreateOrderQR(@RequestBody CreateOrderWrapper order){
        String value = orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList()).toString();

        return ResponseEntity.ok(MatrixToImageWriter.toBufferedImage(qrService.createQR(value)));
    }
    @PutMapping("append-product")
    public ResponseEntity saleAppendProductToOrder(){

        return ResponseEntity.ok("");
    }

    @GetMapping("get-order")
    public ResponseEntity cashierGetPendingOrder(){


        return ResponseEntity.ok("");
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



}
