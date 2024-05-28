package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.entity.Category;
import com.shop.JewleryMS.model.CategoryRequest;
import com.shop.JewleryMS.model.CreateCategoryRequest;
import com.shop.JewleryMS.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(category);
    }
    @GetMapping("/readAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Category>>readAllCategory(){
        List<Category> categoryList = categoryService.readAllCategory();
        return ResponseEntity.ok(categoryList);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@RequestBody Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(categoryRequest);
        return ResponseEntity.ok("Category updated successfully");
    }
}
