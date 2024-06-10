package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryRequest;
import com.project.JewelryMS.model.Category.CategoryResponse;
import com.project.JewelryMS.model.Category.CreateCategoryRequest;
import com.project.JewelryMS.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@SecurityRequirement(name = "api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/create")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/readall")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.readAllCategory();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("read/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.readByIDCategory(id);
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping("/delete")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@RequestBody Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @PutMapping("/update")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(categoryRequest);
        return ResponseEntity.ok("Category updated successfully");
    }
}
