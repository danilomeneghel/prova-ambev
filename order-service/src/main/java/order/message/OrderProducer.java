package order.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import order.dto.OrderCreateDTO;

@Service
public class OrderProducer {

    @Value("${kafka.topic.orders}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, OrderCreateDTO> kafkaTemplate;

    public void sendMessage(OrderCreateDTO message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Produced order message: " + message);
    }

}
