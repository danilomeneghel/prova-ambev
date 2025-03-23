package order.controller;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.service.ProductService;
import order.message.ProductProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductProducer productProducer;

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        if (productService.isProductNumberExists(productCreateDTO.getProductNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product Number already exists");
        }

        productProducer.sendMessage(productCreateDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreateDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
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
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
