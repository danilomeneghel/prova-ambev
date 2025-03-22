package order.controller;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.service.OrderService;
import order.message.Producer;
import order.message.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private Producer producer;
    private Consumer consumer;

    @Autowired
    public OrderController(@Value("${kafka.bootstrap-servers}") String bootstrapServers,
                           @Value("${kafka.order-topic}") String orderTopic,
                           @Value("${kafka.order-group}") String orderGroup) {
        this.producer = new Producer(bootstrapServers, orderTopic);
        this.consumer = new Consumer(bootstrapServers, orderGroup, orderTopic);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderCreateDTO);
        producer.sendMessage("Order created", createdOrder.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderCreateDTO>> filterOrders(@Valid @ModelAttribute OrderCreateDTO orderCreateDTO) {
        List<OrderCreateDTO> filterOrders = orderService.filterOrders(orderCreateDTO);
        producer.sendMessage("filterOrders", orderCreateDTO.toString());
        consumer.consumeMessages();
        return ResponseEntity.status(HttpStatus.OK).body(filterOrders);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        producer.sendMessage("getAllOrders", "Request to get all orders");
        consumer.consumeMessages();
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
