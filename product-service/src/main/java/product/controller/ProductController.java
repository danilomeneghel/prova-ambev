package product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
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

    @Value("${product.service.name}")
    private String serviceName;

    @Value("${product.service.host}")
    private String serviceHost;

    @Value("${product.service.port}")
    private int servicePort;

    @PostConstruct
    public void registerService() throws Exception {
        registry.registerService(serviceName, serviceHost, servicePort);
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
