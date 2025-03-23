package order.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import order.dto.OrderCreateDTO;
import order.service.OrderService;

@Service
public class OrderConsumer {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "${kafka.topic.orders}", groupId = "${kafka.group.order-consumers}", containerFactory = "orderKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, OrderCreateDTO> record) {
        try {
            OrderCreateDTO orderCreateDTO = record.value();
            System.out.println("Consumed order message: " + orderCreateDTO);
            orderService.createOrder(orderCreateDTO);
        } catch (ClassCastException e) {
            System.err.println("Failed to cast message to OrderCreateDTO: " + e.getMessage());
        }
    }
    
}
