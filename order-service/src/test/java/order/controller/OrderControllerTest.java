package order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.message.OrderProducer;
import order.service.OrderService;
import order.discovery.ZookeeperServiceDiscovery;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private ZookeeperServiceDiscovery discovery;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockMvcBuilders.standaloneSetup(orderController).build();
    }
    
    @Test
    public void testCreateOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setOrderNumber(123L);

        when(orderService.isOrderNumberExists(any(Long.class))).thenReturn(false);

        ResponseEntity<Object> response = orderController.createOrder(orderCreateDTO);

        verify(orderProducer, times(1)).sendMessage(any(OrderCreateDTO.class));
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert Objects.equals(response.getBody(), orderCreateDTO);
    }

    @Test
    public void testCreateOrder_OrderNumberExists() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setOrderNumber(123L);

        when(orderService.isOrderNumberExists(any(Long.class))).thenReturn(true);

        ResponseEntity<Object> response = orderController.createOrder(orderCreateDTO);

        assert response.getStatusCode() == HttpStatus.CONFLICT;
        assert response.getBody() != null;
        assert "Order Number already exists".equals(response.getBody());
    }

    @Test
    public void testGetOrderById() {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();

        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert orderDTO.equals(response.getBody());
    }

    @Test
    public void testFilterOrders() {
        OrderFilterDTO orderFilterDTO = new OrderFilterDTO();
        List<OrderFilterDTO> filteredOrders = Arrays.asList(new OrderFilterDTO(), new OrderFilterDTO());

        when(orderService.filterOrders(any(OrderFilterDTO.class))).thenReturn(filteredOrders);

        ResponseEntity<List<OrderFilterDTO>> response = orderController.filterOrders(orderFilterDTO);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert filteredOrders.equals(response.getBody());
    }

    @Test
    public void testGetAllOrders() {
        List<OrderDTO> orders = Arrays.asList(new OrderDTO(), new OrderDTO());

        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert orders.equals(response.getBody());
    }

    @Test
    public void testUpdateOrder() {
        Long orderId = 1L;
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        OrderDTO updatedOrder = new OrderDTO();

        when(orderService.updateOrder(eq(orderId), any(OrderCreateDTO.class))).thenReturn(updatedOrder);

        ResponseEntity<OrderDTO> response = orderController.updateOrder(orderId, orderCreateDTO);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert updatedOrder.equals(response.getBody());
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;

        doNothing().when(orderService).deleteOrder(orderId);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
        verify(orderService, times(1)).deleteOrder(orderId);
    }
    
}
