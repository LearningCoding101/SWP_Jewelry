package com.shop.JewleryMS.Service;

import com.shop.JewleryMS.Entity.Category;
import com.shop.JewleryMS.Model.CreateCategoryRequest;
import com.shop.JewleryMS.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryRequest createCategoryRequest){
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());

        return categoryRepository.save(category);

    }

}
