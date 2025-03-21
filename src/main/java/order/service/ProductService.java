package order.service;

import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.entity.Product;
import order.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ProductDTO getProductById(Long id) {
        return modelMapper.map(productRepository.findById(id).orElse(null), ProductDTO.class);
    }

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

}
