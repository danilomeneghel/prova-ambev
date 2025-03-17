package order.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class OrderDTOTest {

    @Test
    void testOrderDTOCreation() {
        OrderDTO orderDTO = new OrderDTO();
        assertNotNull(orderDTO);
    }

    @Test
    void testOrderDTOProperties() {
        OrderDTO orderDTO = new OrderDTO();
        List<ProductDTO> products = new ArrayList<>();
        ProductDTO product = new ProductDTO();
        products.add(product);
        orderDTO.setProducts(products);
        orderDTO.setId(1L);
        orderDTO.setTotalValue(100.0);

        assertNotNull(orderDTO.getProducts());
        assertEquals(1, orderDTO.getProducts().size());
        assertEquals(product, orderDTO.getProducts().get(0));
        assertEquals(1L, orderDTO.getId());
        assertEquals(100.0, orderDTO.getTotalValue());
    }

    @Test
    void testOrderDTOId() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        assertEquals(1L, orderDTO.getId());
    }

    @Test
    void testOrderDTOProducts() {
        OrderDTO orderDTO = new OrderDTO();
        List<ProductDTO> products = new ArrayList<>();
        ProductDTO product = new ProductDTO();
        products.add(product);
        orderDTO.setProducts(products);

        assertNotNull(orderDTO.getProducts());
        assertEquals(1, orderDTO.getProducts().size());
        assertEquals(product, orderDTO.getProducts().get(0));
    }

    @Test
    void testOrderDTOTotalValue() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalValue(100.0);
        assertEquals(100.0, orderDTO.getTotalValue());
    }
    
}