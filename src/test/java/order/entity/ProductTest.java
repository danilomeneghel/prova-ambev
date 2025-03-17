package order.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    void testProductId() {
        Product product = new Product();
        product.setId(1L);
        assertEquals(1L, product.getId());
    }

    @Test
    void testProductName() {
        Product product = new Product();
        product.setName("Test Product");
        assertEquals("Test Product", product.getName());
    }

    @Test
    void testProductPrice() {
        Product product = new Product();
        product.setPrice(99.99);
        assertEquals(99.99, product.getPrice());
    }
    
}