package order.controller;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.service.ProductService;
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
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testCreateProduct() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        ProductDTO productDTO = new ProductDTO();
        when(productService.createProduct(any(ProductCreateDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(productCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    public void testGetProductById() {
        Long id = 1L;
        ProductDTO productDTO = new ProductDTO();
        when(productService.getProductById(id)).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.getProductById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).getProductById(id);
    }

    @Test
    public void testGetAllProducts() {
        List<ProductDTO> products = Arrays.asList(new ProductDTO(), new ProductDTO());
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testUpdateProduct() {
        Long id = 1L;
        ProductDTO productDTO = new ProductDTO();
        when(productService.updateProduct(eq(id), any(ProductDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(id, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).updateProduct(eq(id), any(ProductDTO.class));
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;
        doNothing().when(productService).deleteProduct(id);

        ResponseEntity<Void> response = productController.deleteProduct(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(id);
    }
}