package order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.dto.ProductCreateDTO;
import order.dto.ProductDTO;
import order.entity.Order;
import order.entity.Product;
import order.repository.OrderRepository;
import order.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {
        Order order = modelMapper.map(orderCreateDTO, Order.class);
        double totalValue = 0;
        List<Product> products = new ArrayList<>();
        for (ProductDTO productDTO : orderCreateDTO.getProducts()) {
            Product product = modelMapper.map(productDTO, Product.class);
            if (productDTO.getId() == null && !productRepository.existsByProductNumber(product.getProductNumber())) {
                product = productRepository.save(product);
            } else {
                product = productRepository.findById(productDTO.getId()).orElse(product);
            }
            products.add(product);
            totalValue += product.getPrice();
        }
        order.setProducts(products);
        order.setTotalValue(totalValue);
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> modelMapper.map(value, OrderDTO.class)).orElse(null);
    }

    public List<OrderFilterDTO> filterOrders(OrderFilterDTO orderFilterDTO) {
        List<Order> orders = orderRepository.findByCriteria(orderFilterDTO);
        return orders.stream().map(order -> modelMapper.map(order, OrderFilterDTO.class)).toList();
    }

    @Cacheable("apiCache")
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrder(Long id, OrderCreateDTO orderCreateDTO) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            double totalValue = 0;
            List<Product> products = new ArrayList<>();
            for (ProductDTO productDTO : orderCreateDTO.getProducts()) {
                Product product = modelMapper.map(productDTO, Product.class);
                if (product.getId() == null) {
                    product = productRepository.save(product);
                } else {
                    product = productRepository.findById(product.getId()).orElse(product);
                }
                products.add(product);
                totalValue += product.getPrice();
            }
            order.setProducts(products);
            order.setTotalValue(totalValue);
            order = orderRepository.save(order);
            return modelMapper.map(order, OrderDTO.class);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public boolean isOrderNumberExists(Long orderNumber) {
        return orderRepository.existsByOrderNumber(orderNumber);
    }

}
