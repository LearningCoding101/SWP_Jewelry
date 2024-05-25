package com.shop.JewleryMS.Controller;

import com.shop.JewleryMS.Entity.Category;
import com.shop.JewleryMS.Model.CreateCategoryRequest;
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




        return ResponseEntity.ok();
    }

    @PostMapping("/Delete")
    public ResponseEntity<String> deleteCategory(){

        return ResponseEntity.ok();
    }

    @PostMapping("/Update")
    public ResponseEntity<String> updateCategory(){

        return ResponseEntity.ok();
    }
}
