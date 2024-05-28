package com.shop.JewleryMS.service;

import com.shop.JewleryMS.config.VNPAYConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service

public class VNPAYservice {

public String createOrder(HttpServletRequest request, int amount, String orderInfo, String urlReturn){
    String vnpVersion = "2.1.0";
    String vnpCommand = "pay";
    String vnpTxnRef = VNPAYConfig.getRandomNumber(8);
    String vnpIpAddr = VNPAYConfig.getIPAddress(request);
    String vnpTmnCode = VNPAYConfig.vnp_TmnCode;
    String orderType = "order-type";
    Map<String, String> vnpParam = new HashMap<>();
    vnpParam.put("vnp_Version", vnpVersion);
    vnpParam.put("vnp_Command", vnpCommand);
    vnpParam.put("vnp_TmnCode", vnpTmnCode);
    vnpParam.put("vnp_Amount", String.valueOf(amount*100));
    vnpParam.put("vnp_CurrCode", "VND");

    vnpParam.put("vnp_TxnRef", vnpTxnRef);
    vnpParam.put("vnp_OrderInfo", orderInfo);
    vnpParam.put("vnp_OrderType", orderType);

    String location = "vn";
    vnpParam.put("vnp_Locale", location);
    urlReturn += VNPAYConfig.vnp_Returnurl;
    vnpParam.put("vnp_ReturUrl", urlReturn);
    vnpParam.put("vnp_IpAddr", vnpIpAddr);
    //set payment creation date with GMT + 7 timezone for vietnam
    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnpCreateDate = formatter.format(cld.getTime());
    vnpParam.put("vnp_CreateDate", vnpCreateDate);

    //set Expiration to be 15 minute
    cld.add(Calendar.MINUTE, 15);
    String vnpExpire = formatter.format(cld.getTime());
    vnpParam.put("vnp_ExpireDate", vnpExpire);

    List fieldNames = new ArrayList(vnpParam.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator itr = fieldNames.iterator();

    while(itr.hasNext()){
        String fieldName = (String) itr.next();
        String fieldValue = (String) vnpParam.get(fieldNames);
        if(
                (fieldName != null) &&
                        (fieldValue.length() > 0)
        ) {
         hashData.append(fieldName);
         hashData.append("=");
         try{
             hashData.append(URLEncoder.encode(
                     fieldValue,
                             StandardCharsets.US_ASCII.toString()
             ));
             query.append(URLEncoder.encode(
                     fieldName,
                     StandardCharsets.US_ASCII.toString()
             ));
             query.append("=");
             query.append(URLEncoder.encode(
                     fieldValue,
                     StandardCharsets.US_ASCII.toString()
             ));

         } catch (UnsupportedEncodingException e){
             System.out.println("ERROR AT VNPAY ENCODING");
             e.printStackTrace();
         }



        }
    }
    return null;


}

}
