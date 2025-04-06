package com.example.ecommerce.controller;

import com.example.ecommerce.config.AppConstants;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
 public ResponseEntity<CategoryResponse> getCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                       @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                       @RequestParam(name = "sortBy" , defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
                                                       @RequestParam(name = "sortOrder",defaultValue =  AppConstants.SORT_DIR) String sortOrder) {
        CategoryResponse categories = categoryService.getCategories(pageNumber,pageSize, sortBy,sortOrder);
        return new ResponseEntity<CategoryResponse>(categories,HttpStatus.OK);
 }

 @PostMapping("/admin/category")
 public ResponseEntity<CategoryDTO> addCategory (@Valid @RequestBody CategoryDTO categoryDTO) {
     System.out.println("Adding category: " + categoryDTO);
       CategoryDTO savedCategoryDTO= categoryService.addCategory(categoryDTO);
     return new ResponseEntity<CategoryDTO>(savedCategoryDTO, HttpStatus.CREATED);

 }


 @PutMapping("/admin/category/{categoryId}")
 public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<CategoryDTO>(categoryService.updateCategory(categoryId,categoryDTO), HttpStatus.OK);
    }


 @DeleteMapping("/admin/category/{categoryId}")
 public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
     return new ResponseEntity<CategoryDTO>(categoryService.deleteCategory(categoryId), HttpStatus.OK);

 }



}
