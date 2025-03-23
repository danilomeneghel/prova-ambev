package order.service;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.entity.Product;
import order.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public ProductDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = modelMapper.map(productCreateDTO, Product.class);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    @Cacheable("apiCache")
    public ProductDTO getProductById(Long id) {
        return modelMapper.map(productRepository.findById(id).orElse(null), ProductDTO.class);
    }

    @Cacheable("apiCache")
    public List<ProductCreateDTO> filterProducts(ProductCreateDTO productCreateDTO) {
        List<Product> products = productRepository.findByCriteria(productCreateDTO);
        return products.stream().map(product -> modelMapper.map(product, ProductCreateDTO.class)).toList();
    }

    @Cacheable("apiCache")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        product.setId(id);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean isProductNumberExists(Long productNumber) {
        return productRepository.existsByProductNumber(productNumber);
    }
    
}
