package order.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringJUnitConfig
@ComponentScan(basePackages = "order")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateOrder() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        when(orderService.createOrder(any(OrderCreateDTO.class))).thenReturn(order);

        mockMvc.perform(post("/order")
                .contentType("application/json")
                .content("{\"description\":\"Test Order\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        when(orderService.updateOrder(eq(1L), any(OrderDTO.class))).thenReturn(order);

        mockMvc.perform(put("/order/1")
                .contentType("application/json")
                .content("{\"description\":\"Updated Order\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/order/1"))
                .andExpect(status().isNoContent());
    }

}
