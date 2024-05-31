package com.project.JewelryMS.service;


import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.CategoryRequest;
import com.project.JewelryMS.model.CreateCategoryRequest;
import com.project.JewelryMS.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(CreateCategoryRequest createCategoryRequest){
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());

        return categoryRepository.save(category);

    }
//    @PostAuthorize("hasRole('STAFF')")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Category> readAllCategory(){
         return categoryRepository.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Category readCategoryById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(CategoryRequest categoryRequest){
        Optional<Category> categoryOpt = categoryRepository.findById(categoryRequest.getId());
        if(categoryOpt.isPresent()){
            Category category = categoryOpt.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            categoryRepository.save(category);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(long id){
        categoryRepository.deleteById(id);
    }
}
