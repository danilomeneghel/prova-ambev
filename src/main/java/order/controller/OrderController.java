package order.controller;

import java.util.List;
import java.util.Optional;

import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import order.discovery.ZookeeperServiceDiscovery;
import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.dto.ProductDTO;
import order.message.OrderProducer;
import order.registry.ZookeeperServiceRegistry;
import order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private ZookeeperServiceRegistry registry;

    private final ZookeeperServiceDiscovery discovery;

    @Value("${zookeeper.service.name}")
    private String serviceName;

    @Value("${zookeeper.service.host}")
    private String serviceHost;

    @Value("${zookeeper.service.port}")
    private int servicePort;

    public OrderController(ZookeeperServiceDiscovery discovery, KafkaTemplate<String, String> kafkaTemplate) {
        this.discovery = discovery;
    }

    @PostConstruct
    public void registerService() throws Exception {
        registry.registerService(serviceName, serviceHost, servicePort);
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        if (orderService.isOrderNumberExists(orderCreateDTO.getOrderNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order Number already exists");
        }

        // Tenta descobrir os serviços registrados
        Optional<ServiceInstance<String>> serviceInstance = discovery.getServiceInstance(serviceName);

        if (orderCreateDTO.getProducts() == null && serviceInstance.isPresent()) {
            // Pega os produtos cadastrados dinamicamente
            String url = String.format("http://%s:%d/product", serviceInstance.get().getAddress(), serviceInstance.get().getPort());
            System.out.println("URL Products: " + url);

            RestTemplate restTemplate = new RestTemplate();
            try {
                List<ProductDTO> products = List.of(restTemplate.getForObject(url, ProductDTO[].class));
                System.out.println("Products :" + products);
                if (!products.isEmpty()) {
                    orderCreateDTO.setProducts(products);
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch products from the API: " + e.getMessage());
            }
        }
        
        orderProducer.sendMessage(orderCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreateDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderFilterDTO>> filterOrders(@Valid @ModelAttribute OrderFilterDTO orderFilterDTO) {
        List<OrderFilterDTO> filterOrders = orderService.filterOrders(orderFilterDTO);
        return ResponseEntity.status(HttpStatus.OK).body(filterOrders);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderCreateDTO orderCreateDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderCreateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    
}
