package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<Product> postProduct(@RequestBody @Valid ProductRecordDto productDto) {
        var productModel = new Product();
        BeanUtils.copyProperties(productDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "id") UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productDto) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto Não Encontrado");
        }
        var productUpdated = product.get();
        BeanUtils.copyProperties(productDto, productUpdated);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productUpdated));
    }
    @DeleteMapping("/products/{id}")
    public  ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id)
    {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto Não Encontrado");
        }
        productRepository.delete(product.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado");
    }
}
