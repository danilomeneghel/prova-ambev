package order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.Valid;
import order.discovery.ZookeeperServiceDiscovery;
import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.dto.ProductDTO;
import order.message.OrderProducer;
import order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private ZookeeperServiceDiscovery serviceDiscovery;

    @Value("${product.service.name}")
    private String productServiceName;

    @PostMapping
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        if (orderService.isOrderNumberExists(orderCreateDTO.getOrderNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order Number already exists");
        }

        try {
            if (orderCreateDTO.getProducts().isEmpty()) {
                String serviceUrl = serviceDiscovery.discoverServiceUrl(productServiceName) + "/product";
                System.out.println("URL Products: " + serviceUrl);

                RestTemplate restTemplate = new RestTemplate();
                List<ProductDTO> products = List.of(restTemplate.getForObject(serviceUrl, ProductDTO[].class));
                System.out.println("Products: " + products);

                if (!products.isEmpty()) {
                    orderCreateDTO.setProducts(products);
                }
            }
        } catch (Exception e) {
            System.err.println("Error find products: " + e.getMessage());
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
