package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.Order.CreateOrderRequest;
import com.project.JewelryMS.model.Order.CreateOrderWrapper;
import com.project.JewelryMS.model.Order.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HtmlFormatterService {
    @Autowired
    CategoryService categoryService;
    public String CreateTableRowOrder(String name,
                                      String cost,
                                      String chi,
                                      String carat,
                                      String manufacturer,
                                      String category,
                                      String quantity
                                      ){

        return "<tr>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + name + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + cost + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + chi + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + carat + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + manufacturer + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + category + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + quantity + "</td>" +
                "</tr>";
    }
    public String DefaultOrderTable(List<ProductResponse> list){
        String tableStart = "<div style='overflow: auto; width: 100%;' role='region' tabindex='0'>" +
                "<table align='center' style='border: 2px solid #dededf; height: 100%; width: 100%; table-layout: fixed; border-collapse: collapse; border-spacing: 1px; text-align: center;'>" +
                "<caption style='caption-side: top; text-align: center;'>Thanh toán</caption>" +
                "<thead>" +
                "<tr>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Tên</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Giá</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Chỉ</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Carat</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Nhà sản xuất</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Thể Loại</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Số lượng</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";

        //Content
        String categoryName = "";
        for(ProductResponse item : list){
            String categoryIfNull = "";
            Category category = categoryService.readCategoryById(item.getCategory_id());
            if (category != null) {
                categoryName = category.getName();
            }
            tableStart += CreateTableRowOrder(
                    item.getName(),
                    String.valueOf(item.getCost()),
                    String.valueOf(item.getChi()),
                    String.valueOf(item.getCarat()),
                    String.valueOf(item.getManufacturer()),
                    categoryName, // categoryName will be empty string if category is null
                    String.valueOf(item.getQuantity())
            );
        }


        //endContent


        tableStart += " </tbody>\n" +
                "</table>\n" +
                "\n" +
                "</div>";


        return tableStart;
    }

}
