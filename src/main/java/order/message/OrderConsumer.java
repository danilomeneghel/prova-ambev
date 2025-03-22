package order.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "${kafka.topic.orders}", groupId = "${kafka.group.order-consumers}")
    public void consume(String message) {
        System.out.println("Consumed order message: " + message);
    }
}
