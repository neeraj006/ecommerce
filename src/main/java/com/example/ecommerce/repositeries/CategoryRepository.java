package com.example.ecommerce.repositeries;

import com.example.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    public Category findByCategoryName(String categoryName);
}
