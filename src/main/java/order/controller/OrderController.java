package order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import order.dto.OrderCreateDTO;
import order.dto.OrderDTO;
import order.dto.OrderFilterDTO;
import order.message.OrderProducer;
import order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;

    @PostMapping
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        if (orderService.isOrderNumberExists(orderCreateDTO.getOrderNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order Number already exists");
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
