package com.shop.JewleryMS.Controller;

import com.shop.JewleryMS.Entity.Category;
import com.shop.JewleryMS.Model.CategoryRequest;
import com.shop.JewleryMS.Model.CreateCategoryRequest;
import com.shop.JewleryMS.Model.RegisterRequest;
import com.shop.JewleryMS.Service.CategoryService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/Create")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(category);
    }
    @GetMapping("/ReadAll")
    public ResponseEntity<List<Category>>readAllCategory(){
        List<Category> categoryList = categoryService.readAllCategory();
        return ResponseEntity.ok(categoryList);
    }

    @PostMapping("/Delete")
    public ResponseEntity<String> deleteCategory(@RequestBody long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @PostMapping("/Update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(categoryRequest);
        return ResponseEntity.ok("Category updated successfully");
    }
}
