package order.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import order.dto.ProductCreateDTO;

@Service
public class ProductProducer {

    @Value("${kafka.topic.products}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, ProductCreateDTO> kafkaTemplate;

    public void sendMessage(ProductCreateDTO message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Produced product message: " + message);
    }
    
}
