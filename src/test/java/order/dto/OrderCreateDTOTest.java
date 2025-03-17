package order.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class OrderCreateDTOTest {

    @Test
    void testOrderCreateDTOCreation() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        assertNotNull(orderCreateDTO);
    }

    @Test
    void testOrderCreateDTOProperties() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        List<ProductCreateDTO> products = new ArrayList<>();
        ProductCreateDTO product = new ProductCreateDTO();
        products.add(product);
        orderCreateDTO.setProducts(products);

        assertNotNull(orderCreateDTO.getProducts());
        assertEquals(1, orderCreateDTO.getProducts().size());
        assertEquals(product, orderCreateDTO.getProducts().get(0));
    }

}