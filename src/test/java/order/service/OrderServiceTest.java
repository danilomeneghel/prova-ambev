package order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.entity.Order;
import order.entity.Product;
import order.repository.OrderRepository;
import order.repository.ProductRepository;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        Order order = new Order();
        Product product = new Product();
        product.setPrice(100.0);
        List<Product> products = new ArrayList<>();
        products.add(product);
        order.setProducts(products);
        OrderDTO orderDTO = new OrderDTO();

        when(modelMapper.map(orderCreateDTO, Order.class)).thenReturn(order);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderCreateDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        orders.add(order);
        OrderDTO orderDTO = new OrderDTO();

        when(orderRepository.findAll()).thenReturn(orders);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        Long id = 1L;
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(id);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateOrder() {
        Long id = 1L;
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.updateOrder(id, orderDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrder() {
        Long id = 1L;

        doNothing().when(orderRepository).deleteById(id);

        orderService.deleteOrder(id);

        verify(orderRepository, times(1)).deleteById(id);
    }

}