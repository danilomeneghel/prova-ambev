package order.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.Optional; // Add this import
import org.junit.jupiter.api.Assertions; // Add this import

import order.entity.Order;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringJUnitConfig
@ComponentScan(basePackages = "order")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveOrder() {
        Order order = new Order();
        Order savedOrder = orderRepository.save(order);
        assertNotNull(savedOrder);
        assertEquals(order, savedOrder);
    }

    @Test
    public void testFindOrderById() {
        Order order = new Order();
        order.setTotalValue(100.0);
        orderRepository.save(order);

        Optional<Order> foundOrder = orderRepository.findById(order.getId());
        Assertions.assertNotNull(foundOrder.orElse(null));
    }

}
