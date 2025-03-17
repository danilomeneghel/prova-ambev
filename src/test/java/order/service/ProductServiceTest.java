package order.service;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.entity.Product;
import order.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;
    private ProductCreateDTO productCreateDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productCreateDTO = new ProductCreateDTO();
    }

    @Test
    void testCreateProduct() {
        when(modelMapper.map(productCreateDTO, Product.class)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductDTO createdProduct = productService.createProduct(productCreateDTO);

        assertNotNull(createdProduct);
        assertEquals(1L, createdProduct.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductDTO foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        List<ProductDTO> productDTOList = productService.getAllProducts();

        assertNotNull(productDTOList);
        assertEquals(1, productDTOList.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        when(modelMapper.map(productDTO, Product.class)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductDTO updatedProduct = productService.updateProduct(1L, productDTO);

        assertNotNull(updatedProduct);
        assertEquals(1L, updatedProduct.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
    
}