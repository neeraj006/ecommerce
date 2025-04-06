package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryService {

    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    public CategoryDTO addCategory(CategoryDTO categoryDTO);

    public CategoryDTO updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO category);

    public CategoryDTO deleteCategory( Long categoryId);




    }
