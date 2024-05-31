package com.project.JewelryMS.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Component
public class VNPAYConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_Returnurl = "/vnpay-payment-return";
    public static String vnp_TmnCode = "8I7ETFZ8"; // kiểm tra email sau
    public static String vnp_HashSecret = "OY5H5MK5YH3S5FOAI8OHEFLBLTQNMIXR"; // khi đăng ký Test
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String hashAllFields(Map fields){

        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();

        while(itr.hasNext()){
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if((fieldValue != null) &&
                    (fieldValue.length() > 0)
            ) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);

            }
            if (itr.hasNext()){
                sb.append("&");
            }
        }
        return hmacSHA512(vnp_HashSecret, sb.toString());

    }

    public static String hmacSHA512(final String key, final String data){
        try{
            if(key == null || data == null){
                System.out.println("hmacSHA512: key or data parameter is null");
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder( 2 * result.length);
            for (byte b : result){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();



        } catch (Exception ex){
            System.out.println("VNPAYconfig hmacSHA512 returned empty");
            return "";
        }



    }

    public static String getIPAddress(HttpServletRequest request){
        String ipAddress;
        try{
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null){
                ipAddress = request.getLocalAddr();
            }
        } catch(Exception e){
            ipAddress = "Invalid IP:" + e.getMessage();
            System.out.println("ERROR OCCURED getIPAddress:" + e.getMessage());
        }
        return ipAddress;
    }

    public static String getRandomNumber(int len){
        Random ran = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);

        for(int i = 0; i < len; i++){
            sb.append(
                    chars.charAt(
                            ran.nextInt(
                                    chars.length()
                            )
                    )
            );
        }
        return sb.toString();
    }

}