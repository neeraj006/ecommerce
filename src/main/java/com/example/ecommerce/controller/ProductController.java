package com.example.ecommerce.controller;


import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {

        ProductDTO productDTOResponse = productService.addProduct(categoryId,productDTO);

        return new ResponseEntity<>(productDTOResponse, HttpStatus.CREATED);

    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {

        ProductResponse productResponse = productService.getAllProducts();

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId  ) {

        ProductResponse productResponse = productService.getProductsByCategory(categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String keyword  ) {

        ProductResponse productResponse = productService.getProductsByKeywordName(keyword);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId ,@Valid @RequestBody ProductDTO productDTO ) {

        ProductDTO productDTOResponse = productService.updateProduct(productId,productDTO);

        return new ResponseEntity<>(productDTOResponse, HttpStatus.OK);

    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId  ) {

        ProductDTO productDTO = productService.deleteProduct(productId);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }
    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId , @RequestParam("image") MultipartFile image ) throws IOException {

        ProductDTO updatedProduct = productService.updateProductImage(productId, image);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }
}
