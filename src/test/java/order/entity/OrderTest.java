package order.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order();
        assertNotNull(order);
    }

    @Test
    void testOrderProperties() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);
        order.setProducts(products);
        order.setId(1L);
        order.setTotalValue(100.0);

        assertNotNull(order.getProducts());
        assertEquals(1, order.getProducts().size());
        assertEquals(product, order.getProducts().get(0));
        assertEquals(1L, order.getId());
        assertEquals(100.0, order.getTotalValue());
    }

    @Test
    void testOrderId() {
        Order order = new Order();
        order.setId(1L);
        assertEquals(1L, order.getId());
    }

    @Test
    void testOrderProducts() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);
        order.setProducts(products);

        assertNotNull(order.getProducts());
        assertEquals(1, order.getProducts().size());
        assertEquals(product, order.getProducts().get(0));
    }

    @Test
    void testOrderTotalValue() {
        Order order = new Order();
        order.setTotalValue(100.0);
        assertEquals(100.0, order.getTotalValue());
    }
    
}