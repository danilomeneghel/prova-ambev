package order.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import order.entity.Order;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testExistsByOrderNumber() {
        Order order = new Order();
        order.setOrderNumber(123L);
        order.setStatus("NEW"); 
        orderRepository.save(order);

        boolean exists = orderRepository.existsByOrderNumber(123L);

        assertTrue(exists);
    }
}
