package order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.message.ProductProducer;
import order.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductProducer productProducer;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductNumber(123L);

        when(productService.isProductNumberExists(any())).thenReturn(false);

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productNumber").value("123"));
    }

    @Test
    public void testGetProductById() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);

        when(productService.getProductById(anyLong())).thenReturn(productDTO);

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testFilterProducts() throws Exception {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        List<ProductCreateDTO> products = Arrays.asList(productCreateDTO);

        when(productService.filterProducts(any())).thenReturn(products);

        mockMvc.perform(get("/product/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        List<ProductDTO> products = Arrays.asList(productDTO);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);

        when(productService.updateProduct(anyLong(), any())).thenReturn(productDTO);

        mockMvc.perform(put("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isNoContent());
    }
    
}
