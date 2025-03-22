package order.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    @KafkaListener(topics = "${kafka.topic.products}", groupId = "${kafka.group.product-consumers}")
    public void consume(String message) {
        System.out.println("Consumed product message: " + message);
    }
}
