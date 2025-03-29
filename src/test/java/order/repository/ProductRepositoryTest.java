package order.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import order.entity.Product;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testExistsByProductNumber() {
        Product product = new Product();
        product.setProductNumber(123L);
        product.setPrice(100.0); 
        productRepository.save(product);

        boolean exists = productRepository.existsByProductNumber(123L);

        assertTrue(exists);
    }
}
