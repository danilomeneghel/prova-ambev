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

    @KafkaListener(topics = "${kafka.topic.orders}", groupId = "${kafka.group.order-consumers}")
    public void consume(ConsumerRecord<String, OrderCreateDTO> record) {
        OrderCreateDTO orderCreateDTO = record.value();
        System.out.println("Mensagem recebida do Kafka: " + orderCreateDTO);

        orderService.createOrder(orderCreateDTO);
    }

}
