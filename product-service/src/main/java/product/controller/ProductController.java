package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.util.List;
import product.dto.ProductCreateDTO;
import product.dto.ProductDTO;
import product.registry.ZookeeperServiceRegistry;
import product.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ZookeeperServiceRegistry registry;

    @PostConstruct
    public void registerService() throws Exception {
        registry.registerService(); 
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        if (productService.isProductNumberExists(productCreateDTO.getProductNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product Number already exists");
        }

        try {
            productService.createProduct(productCreateDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreateDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    
}
