package order.controller;

import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.service.OrderService;
import order.message.OrderProducer;
import order.message.OrderConsumer;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private OrderConsumer orderConsumer;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        if (orderCreateDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order fields cannot be empty");
        }

        if (orderService.isOrderNumberExists(orderCreateDTO.getOrderNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order Number already exists");
        }

        orderProducer.sendMessage(orderCreateDTO.toString());
        OrderDTO createdOrder = orderService.createOrder(orderCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        orderConsumer.consume("Fetching order by id: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderFilterDTO>> filterOrders(@Valid @ModelAttribute OrderFilterDTO orderFilterDTO) {
        List<OrderFilterDTO> filterOrders = orderService.filterOrders(orderFilterDTO);
        orderConsumer.consume("Filtering orders");
        return ResponseEntity.status(HttpStatus.OK).body(filterOrders);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        orderConsumer.consume("Fetching all orders");
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
