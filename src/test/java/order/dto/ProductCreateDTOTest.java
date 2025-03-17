package order.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ProductCreateDTOTest {

    @Test
    void testProductCreateDTOCreation() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        assertNotNull(productCreateDTO);
    }

    @Test
    void testProductCreateDTOProperties() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setName("Test Product");
        productCreateDTO.setPrice(99.99);

        assertEquals("Test Product", productCreateDTO.getName());
        assertEquals(99.99, productCreateDTO.getPrice());
    }

    @Test
    void testProductCreateDTOName() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setName("Test Product");
        assertEquals("Test Product", productCreateDTO.getName());
    }

    @Test
    void testProductCreateDTOPrice() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setPrice(99.99);
        assertEquals(99.99, productCreateDTO.getPrice());
    }
    
}