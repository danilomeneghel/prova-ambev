package order.controller;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.createOrder(any(OrderCreateDTO.class))).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.createOrder(orderCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).createOrder(any(OrderCreateDTO.class));
    }

    @Test
    public void testGetOrderById() {
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.getOrderById(anyLong())).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).getOrderById(anyLong());
    }

    @Test
    public void testFilterOrders() {
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orderDTOList = Arrays.asList(orderDTO);
        when(orderService.filterOrders(any(OrderDTO.class))).thenReturn(orderDTOList);

        ResponseEntity<List<OrderDTO>> response = orderController.filterOrders(orderDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTOList, response.getBody());
        verify(orderService, times(1)).filterOrders(any(OrderDTO.class));
    }

    @Test
    public void testGetAllOrders() {
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orderDTOList = Arrays.asList(orderDTO);
        when(orderService.getAllOrders()).thenReturn(orderDTOList);

        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTOList, response.getBody());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void testUpdateOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.updateOrder(anyLong(), any(OrderCreateDTO.class))).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.updateOrder(1L, orderCreateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).updateOrder(anyLong(), any(OrderCreateDTO.class));
    }

    @Test
    public void testDeleteOrder() {
        doNothing().when(orderService).deleteOrder(anyLong());

        ResponseEntity<Void> response = orderController.deleteOrder(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(anyLong());
    }
}