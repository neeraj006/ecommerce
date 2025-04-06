package com.example.ecommerce.service;

import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.repositeries.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy , String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?  Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();

        if(categories.isEmpty()) {
            throw new ApiException( "There are no existing categories");
        }

        List<CategoryDTO> categoryDTOS  = categories.stream().map(
                category -> modelMapper.map(category, CategoryDTO.class)

        ).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        // Validate if the category name is already present (clear logic for duplicate handling)
        if (categoryRepository.findByCategoryName(categoryDTO.getCategoryName()) != null) {
            throw new ApiException(
                    "Category with the name '" + categoryDTO.getCategoryName() + "' already exists."
            );
        }

        // Map DTO -> Entity
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        System.out.println("Category to be added: " + category);

        // Save the new category and return as DTO
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = new CategoryDTO(
                savedCategory.getCategoryId(),
                savedCategory.getCategoryName()
        );

        System.out.println("Category added: " + savedCategory);

        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        category.setCategoryId(categoryId);
        Category savedCategory =  categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
      Category deletedCategory=  categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));


          categoryRepository.delete(deletedCategory);


        return modelMapper.map(deletedCategory, CategoryDTO.class);
    }


}
