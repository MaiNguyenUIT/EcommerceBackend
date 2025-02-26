package com.example.backend.serviceImpl;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.ENUM.PRODUCT_STATE;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService implements com.example.backend.service.ProductService {
    @Autowired
    private ProductRepository productRepository;
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
        existProduct.setCategoryName(productDTO.getCategoryName());
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
    public Product getProduct(String id) {
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return existProduct;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> filterProductByCategory(String categoryName) {
        return productRepository.findBycategoryName(categoryName);
    }

    @Override
    public List<Product> getAllActiveProduct() {
        return productRepository.findByproductState(PRODUCT_STATE.ACTIVE);
    }
}
