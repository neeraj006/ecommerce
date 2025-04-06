package com.example.ecommerce.service;

import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.repositeries.CategoryRepository;
import com.example.ecommerce.repositeries.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl  implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.images}")
    String imageDirPath;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
       Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));

       boolean isProductAlreadyExists = false;
       List<Product> products = category.getProducts();

       for(Product p: products){
           if(p.getProductName().equals(product.getProductName())){
               isProductAlreadyExists = true;
               break;
           }

       }

       if(isProductAlreadyExists){
           throw new ApiException("Product already exists");
       }
       else {
           product.setCategory(category);
           product.setImage("default.png");

           double specialPrice = product.getPrice() - (product.getDiscount() * 0.01 * product.getPrice());
           product.setSpecialPrice(specialPrice);
           Product savedProduct=  productRepository.save(product);

           return modelMapper.map(savedProduct, ProductDTO.class);
       }


    }

    @Override
    public ProductResponse getAllProducts() {
       List<Product>  productList=   productRepository.findAll();
       List<ProductDTO>  productDTOList = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        return  new ProductResponse(productDTOList);
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product>  productList=   productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO>  productDTOList = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        return  new ProductResponse(productDTOList);
    }

    @Override
    public ProductResponse getProductsByKeywordName(String keyword) {
        List<Product>  productList=   productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%");
        List<ProductDTO>  productDTOList = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        return  new ProductResponse(productDTOList);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB= productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        Product product = modelMapper.map(productDTO, Product.class);

        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(product.getSpecialPrice());
        Product updatedProduct=  productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productFromDB= productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.deleteById(productId);
        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // get the product from DB
     Product productDB=    productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        // upload image to server
     String fileName=   fileService.uploadImage(image,imageDirPath);

        // get the image name
        // update the product with the image name
        productDB.setImage(fileName);

        // save the product and return DTO
      Product updatedProduct =  productRepository.save(productDB);
        return modelMapper.map(productDB, ProductDTO.class);
    }




}
