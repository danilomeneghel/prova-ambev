package order.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import order.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringJUnitConfig
@ComponentScan(basePackages = "order")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        Product product = new Product();
        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
    }

    @Test
    void testFindProductById() {
        Product product = new Product();
        product.setId(1L);
        productRepository.save(product);
        Product foundProduct = productRepository.findById(1L).orElse(null);
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
    }

}
