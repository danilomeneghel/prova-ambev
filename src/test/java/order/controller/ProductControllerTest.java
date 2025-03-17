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

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringJUnitConfig
@ComponentScan(basePackages = "order")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProductById() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        when(productService.createProduct(any(ProductCreateDTO.class))).thenReturn(product);

        mockMvc.perform(post("/product")
                .contentType("application/json")
                .content("{\"name\":\"Test Product\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(product);

        mockMvc.perform(put("/product/1")
                .contentType("application/json")
                .content("{\"name\":\"Updated Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isNoContent());
    }

}
