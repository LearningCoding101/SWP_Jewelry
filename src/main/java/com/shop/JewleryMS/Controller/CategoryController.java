package com.shop.JewleryMS.Controller;

import com.shop.JewleryMS.Entity.Category;
import com.shop.JewleryMS.Model.CreateCategoryRequest;
import com.shop.JewleryMS.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("testCategory")
    public ResponseEntity test(){
        System.out.println("pinged createProduct");

        return ResponseEntity.ok("ok");

    }
    @PostMapping("/CreateCategory")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(category);
    }
}
