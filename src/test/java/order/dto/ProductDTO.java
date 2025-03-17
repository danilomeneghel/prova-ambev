package order.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ProductDTOTest {

    @Test
    void testProductDTOCreation() {
        ProductDTO productDTO = new ProductDTO();
        assertNotNull(productDTO);
    }

    @Test
    void testProductDTOProperties() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setPrice(10.0);

        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertEquals(10.0, productDTO.getPrice());
    }

    @Test
    void testProductDTOId() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        assertEquals(1L, productDTO.getId());
    }

    @Test
    void testProductDTOName() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        assertEquals("Test Product", productDTO.getName());
    }

    @Test
    void testProductDTOPrice() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrice(10.0);
        assertEquals(10.0, productDTO.getPrice());
    }
    
}