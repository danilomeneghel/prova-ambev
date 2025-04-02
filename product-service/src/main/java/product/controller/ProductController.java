package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import product.dto.ProductCreateDTO;
import product.dto.ProductDTO;
import product.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        if (productService.isProductNumberExists(productCreateDTO.getProductNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product Number already exists");
        }
        productService.createProduct(productCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreateDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductCreateDTO>> filterProducts(@Valid @ModelAttribute ProductCreateDTO productCreateDTO) {
        List<ProductCreateDTO> filterProducts = productService.filterProducts(productCreateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(filterProducts);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductCreateDTO productCreateDTO) {
        if (!productService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productService.updateProduct(id, productCreateDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
