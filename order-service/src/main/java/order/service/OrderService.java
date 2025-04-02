package order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import order.discovery.ZookeeperServiceDiscovery;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.dto.ProductCreateDTO;
import order.entity.Order;
import order.entity.Product;
import order.repository.OrderRepository;
import order.repository.ProductRepository;
import order.message.OrderProducer;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private ZookeeperServiceDiscovery serviceDiscovery;

    private ModelMapper modelMapper = new ModelMapper();

    public void sendOrder(OrderCreateDTO orderCreateDTO) {
        try {
            if (orderCreateDTO.getProducts().isEmpty()) {
                String serviceUrl = serviceDiscovery.discoverServiceUrl("product-service") + "/product";
                RestTemplate restTemplate = new RestTemplate();
                List<ProductCreateDTO> products = List.of(restTemplate.getForObject(serviceUrl, ProductCreateDTO[].class));
                System.out.println("Products: " + products);
                if (!products.isEmpty()) {
                    orderCreateDTO.setProducts(products);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve products: " + e.getMessage());
        }
        orderProducer.sendMessage(orderCreateDTO);
    }

    @Transactional
    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {
        Order order = modelMapper.map(orderCreateDTO, Order.class);
        double totalValue = 0;
        List<Product> products = new ArrayList<>();

        for (ProductCreateDTO productCreateDTO : orderCreateDTO.getProducts()) {
            Product product = productRepository.findByProductNumber(productCreateDTO.getProductNumber())
                                               .orElseGet(() -> productRepository.save(modelMapper.map(productCreateDTO, Product.class)));

            if (!products.contains(product)) {
                products.add(product);
                totalValue += product.getPrice();
            }
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

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrder(Long id, OrderCreateDTO orderCreateDTO) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            double totalValue = 0;
            List<Product> products = new ArrayList<>();

            for (ProductCreateDTO productCreateDTO : orderCreateDTO.getProducts()) {
                Product product = productRepository.findByProductNumber(productCreateDTO.getProductNumber())
                                                   .orElseGet(() -> productRepository.save(modelMapper.map(productCreateDTO, Product.class)));

                if (!products.contains(product)) {
                    products.add(product);
                    totalValue += product.getPrice();
                }
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

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    public boolean isOrderNumberExists(Long orderNumber) {
        return orderRepository.existsByOrderNumber(orderNumber);
    }
    
}
