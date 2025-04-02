package product.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import product.dto.ProductCreateDTO;
import product.dto.ProductDTO;
import product.entity.Product;
import product.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public ProductDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = modelMapper.map(productCreateDTO, Product.class);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    public ProductDTO getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> modelMapper.map(value, ProductDTO.class)).orElse(null);
    }

    public List<ProductCreateDTO> filterProducts(ProductCreateDTO productCreateDTO) {
        List<Product> products = productRepository.findByCriteria(productCreateDTO);
        return products.stream().map(product -> modelMapper.map(product, ProductCreateDTO.class)).toList();
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductCreateDTO updateProduct(Long id, ProductCreateDTO productCreateDTO) {
        Product product = modelMapper.map(productCreateDTO, Product.class);
        product.setId(id);
        return modelMapper.map(productRepository.save(product), ProductCreateDTO.class);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
    
    public boolean isProductNumberExists(Long productNumber) {
        return productRepository.existsByProductNumber(productNumber);
    }
    
}
