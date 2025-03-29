package order.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.entity.Order;
import order.repository.OrderRepository;
import order.repository.ProductRepository;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        // ...configure orderCreateDTO...

        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.createOrder(orderCreateDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testUpdateOrder() {
        Long orderId = 1L;
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.updateOrder(orderId, orderCreateDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;

        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testIsOrderNumberExists() {
        Long orderNumber = 123L;
        when(orderRepository.existsByOrderNumber(orderNumber)).thenReturn(true);

        boolean exists = orderService.isOrderNumberExists(orderNumber);

        assertTrue(exists);
        verify(orderRepository, times(1)).existsByOrderNumber(orderNumber);
    }
}
