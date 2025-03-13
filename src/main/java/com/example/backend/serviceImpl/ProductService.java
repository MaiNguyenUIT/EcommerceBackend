package com.example.backend.serviceImpl;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.DTO.response.ProductResponse;
import com.example.backend.ENUM.PRODUCT_STATE;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.mapper.ResponseMapper.ProductResMapper;
import com.example.backend.model.Category;
import com.example.backend.model.Product;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService implements com.example.backend.service.ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product existProduct = productRepository.findByname(productDTO.getName());
        if(existProduct != null){
            throw new BadRequestException("This product is already exist, please use update product");
        }
        if(productDTO.getQuantity() < 0){
            throw new BadRequestException("Product quantity must be more than 0");
        }
        if(productDTO.getPrice() < 0){
            throw new BadRequestException("Product price must be more than 0");
        }
        if(productDTO.getRegularPrice() < 0){
            throw new BadRequestException("Product regular price must be more than 0");
        }
        if(productDTO.getQuantity() > 0){
            productDTO.setProductState(PRODUCT_STATE.ACTIVE);
        } else {
            productDTO.setProductState(PRODUCT_STATE.HIDDEN);
        }
        Product product = ProductMapper.INSTANCE.toEntity(productDTO);
        productRepository.save(product);
        return productDTO;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, String id) {
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if(productRepository.existsByNameAndNotId(productDTO.getName(), id)){
            throw new BadRequestException("This product name is already exist");
        }
        if(productDTO.getQuantity() < 0){
            throw new BadRequestException("Product quantity must be more than 0");
        }
        if(productDTO.getPrice() < 0){
            throw new BadRequestException("Product price must be more than 0");
        }
        if(productDTO.getRegularPrice() < 0){
            throw new BadRequestException("Product regular price must be more than 0");
        }

        existProduct.setPrice(productDTO.getPrice());
        existProduct.setName(productDTO.getName());
        existProduct.setDescription(productDTO.getDescription());
        existProduct.setImage(productDTO.getImage());
        existProduct.setCategoryId(productDTO.getCategoryId());
        existProduct.setRegularPrice(productDTO.getRegularPrice());
        existProduct.setQuantity(productDTO.getQuantity());
        existProduct.setProductState(productDTO.getProductState());

        return ProductMapper.INSTANCE.toDTO(productRepository.save(existProduct));
    }

    @Override
    public void deleteProduct(String id) {
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        existProduct.setProductState(PRODUCT_STATE.HIDDEN);
        productRepository.save(existProduct);
    }

    @Override
    public ProductResponse getProduct(String id) {
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        ProductResponse productResponse = ProductResMapper.INSTANCE.toRes(existProduct);
        productResponse.setCategoryName(categoryRepository.findById(existProduct.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found")).getTitle());
        return productResponse;
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();

        List<String> categoryIds = products.stream()
                .map(Product::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getTitle));

        return products.stream().map(product -> {
            ProductResponse productResponse = ProductResMapper.INSTANCE.toRes(product);
            productResponse.setCategoryName(categoryMap.get(product.getCategoryId())); // Lấy tên category từ Map
            return productResponse;
        }).collect(Collectors.toList());
    }


    @Override
    public List<ProductResponse> filterProductByCategory(String categoryId) {
        List<Product> products = productRepository.findBycategoryId(categoryId);

        List<String> categoryIds = products.stream()
                .map(Product::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getTitle));

        return products.stream().map(product -> {
            ProductResponse productResponse = ProductResMapper.INSTANCE.toRes(product);
            productResponse.setCategoryName(categoryMap.get(product.getCategoryId())); // Lấy tên category từ Map
            return productResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllActiveProduct() {
        List<Product> products = productRepository.findByproductState(PRODUCT_STATE.ACTIVE);
        List<String> categoryIds = products.stream()
                .map(Product::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        Map<String, String> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getTitle));

        return products.stream().map(product -> {
            ProductResponse productResponse = ProductResMapper.INSTANCE.toRes(product);
            productResponse.setCategoryName(categoryMap.get(product.getCategoryId())); // Lấy tên category từ Map
            return productResponse;
        }).collect(Collectors.toList());
    }
}
