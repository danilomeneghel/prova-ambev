package order.controller;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderCreateDTO orderCreateDTO;
    private OrderDTO orderDTO;

    @BeforeEach
    void setup() {
        orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setOrderNumber(12345L); 

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setOrderNumber(12345L); 
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        when(orderService.isOrderNumberExists(orderCreateDTO.getOrderNumber())).thenReturn(false);

        ResponseEntity<Object> response = orderController.createOrder(orderCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderCreateDTO, response.getBody());
        verify(orderService, times(1)).sendOrder(orderCreateDTO);
    }

    @Test
    void shouldReturnConflictWhenOrderAlreadyExists() {
        when(orderService.isOrderNumberExists(orderCreateDTO.getOrderNumber())).thenReturn(true);

        ResponseEntity<Object> response = orderController.createOrder(orderCreateDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Order Number already exists", response.getBody());
        verify(orderService, never()).sendOrder(any());
    }

    @Test
    void shouldReturnOrderByIdSuccessfully() {
        when(orderService.existsById(1L)).thenReturn(true);
        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        ResponseEntity<Object> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        when(orderService.existsById(1L)).thenReturn(false);

        ResponseEntity<Object> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Order not found", response.getBody());
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        when(orderService.existsById(1L)).thenReturn(true);
        when(orderService.updateOrder(eq(1L), any())).thenReturn(orderDTO);

        ResponseEntity<Object> response = orderController.updateOrder(1L, orderCreateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingOrder() {
        when(orderService.existsById(1L)).thenReturn(false);

        ResponseEntity<Object> response = orderController.updateOrder(1L, orderCreateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Order not found", response.getBody());
    }

    @Test
    void shouldDeleteOrderSuccessfully() {
        when(orderService.existsById(1L)).thenReturn(true);

        ResponseEntity<Object> response = orderController.deleteOrder(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingOrder() {
        when(orderService.existsById(1L)).thenReturn(false);

        ResponseEntity<Object> response = orderController.deleteOrder(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Order not found", response.getBody());
    }
}
