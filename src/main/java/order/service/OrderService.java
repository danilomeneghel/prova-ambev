package order.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.entity.Order;
import order.entity.Product;
import order.repository.OrderRepository;
import order.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {
        Order order = modelMapper.map(orderCreateDTO, Order.class);
        double totalValue = 0;
        for (Product product : order.getProducts()) {
            if (product.getId() == null) {
                productRepository.save(product);
            } else {
                product = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
            }
            totalValue += product.getPrice();
        }
        order.setTotalValue(totalValue);
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                     .map(order -> modelMapper.map(order, OrderDTO.class))
                     .toList();
    }

    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> modelMapper.map(value, OrderDTO.class)).orElse(null);
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            modelMapper.map(orderDTO, order);
            order = orderRepository.save(order);
            return modelMapper.map(order, OrderDTO.class);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
