package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.CategoryRequest;
import com.project.JewelryMS.model.CreateCategoryRequest;
import com.project.JewelryMS.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@SecurityRequirement(name = "api")
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
        System.out.println("pinged readall");
        return ResponseEntity.ok(categoryService.readAllCategory());
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
