package order.controller;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.service.ProductService;
import order.message.Producer;
import order.message.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private Producer producer;
    private Consumer consumer;

    @Autowired
    public ProductController(@Value("${kafka.bootstrap-servers}") String bootstrapServers,
                             @Value("${kafka.product-topic}") String productTopic,
                             @Value("${kafka.product-group}") String productGroup) {
        this.producer = new Producer(bootstrapServers, productTopic);
        this.consumer = new Consumer(bootstrapServers, productGroup, productTopic);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        ProductDTO createdProduct = productService.createProduct(productCreateDTO);
        producer.sendMessage("Product created", createdProduct.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductCreateDTO>> filterProducts(@Valid @ModelAttribute ProductCreateDTO productCreateDTO) {
        List<ProductCreateDTO> filterProducts = productService.filterProducts(productCreateDTO);
        producer.sendMessage("filterProducts", productCreateDTO.toString());
        consumer.consumeMessages();
        return ResponseEntity.status(HttpStatus.OK).body(filterProducts);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        producer.sendMessage("getAllProducts", "Request to get all products");
        consumer.consumeMessages();
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
