package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.model.Category.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT new com.project.JewelryMS.model.Category.CategoryResponse(c.id, c.description, c.name) FROM Category c WHERE c.status = 1")
    List<CategoryResponse> findAllCategories();

    @Query("SELECT new com.project.JewelryMS.model.Category.CategoryResponse(c.id, c.description, c.name) FROM Category c WHERE c.id = :id AND c.status = 1")
    Optional<CategoryResponse> findCategoryById(Long id);
}
