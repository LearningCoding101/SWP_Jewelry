package com.shop.JewleryMS.Service;

import com.shop.JewleryMS.Entity.Category;
import com.shop.JewleryMS.Model.CategoryRequest;
import com.shop.JewleryMS.Model.CreateCategoryRequest;
import com.shop.JewleryMS.repository.CategoryRepository;
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

        return categoryRepository.save(category);

    }

   public List<Category> readAllCategory(){
        List<Category> allCategory = categoryRepository.findAll();
        return allCategory;
    }
    public Category readCategoryById(Long id){
        return categoryRepository.findById(id).orElse(null);
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
        categoryRepository.deleteById(id);
    }
}
