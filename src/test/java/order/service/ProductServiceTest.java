package order.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.entity.Product;
import order.repository.ProductRepository;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        // ...configure productCreateDTO...

        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.createProduct(productCreateDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testGetProductById() {
        Long productId = 1L;
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(productId);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getAllProducts();

        assertNotNull(result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testIsProductNumberExists() {
        Long productNumber = 123L;
        when(productRepository.existsByProductNumber(productNumber)).thenReturn(true);

        boolean exists = productService.isProductNumberExists(productNumber);

        assertTrue(exists);
        verify(productRepository, times(1)).existsByProductNumber(productNumber);
    }
}
