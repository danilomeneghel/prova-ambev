package order.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import order.dto.ProductCreateDTO;
import order.service.ProductService;

@Service
public class ProductConsumer {

    @Autowired
    private ProductService productService;

    @KafkaListener(topics = "${kafka.topic.products}", groupId = "${kafka.group.product-consumers}")
    public void consume(ConsumerRecord<String, ProductCreateDTO> record) {
        ProductCreateDTO productCreateDTO = record.value();
        System.out.println("Mensagem recebida do Kafka: " + productCreateDTO);

        productService.createProduct(productCreateDTO);
    }

}
