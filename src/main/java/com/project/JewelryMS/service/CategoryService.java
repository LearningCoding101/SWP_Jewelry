package com.project.JewelryMS.service;


import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryRequest;
import com.project.JewelryMS.model.Category.CategoryResponse;
import com.project.JewelryMS.model.Category.CreateCategoryRequest;
import com.project.JewelryMS.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public Category createCategory(CreateCategoryRequest createCategoryRequest){
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());
        category.setStatus(1);
        return categoryRepository.save(category);

    }

    public List<CategoryResponse> readAllCategory(){
        return categoryRepository.findAllCategories();
    }

    public CategoryResponse readByIDCategory(Long id){
        Optional<CategoryResponse> categoryResponseOptional = categoryRepository.findCategoryById(id);
        return categoryResponseOptional.get();
    }


    public Category readCategoryById(Long id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.get();
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }
    public void updateCategory(CategoryRequest categoryRequest){
        Optional<Category> categoryOpt = categoryRepository.findById(categoryRequest.getId());
        if(categoryOpt.isPresent()){
            Category category = categoryOpt.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            categoryRepository.save(category);
        }
    }
    public void deleteCategory(long id){
        Optional<Category> productSellOptional = categoryRepository.findById(id);
        if(productSellOptional.isPresent()){
            Category category = productSellOptional.get();
            category.setStatus(0);
            categoryRepository.save(category);
        }else {
            throw new RuntimeException("Product Sell with ID " + id + " not found");
        }
    }
}
