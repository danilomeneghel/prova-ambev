package order.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderCreateDTO {

    private Long orderNumber;
    
    private LocalDateTime dateTime;
    
    private String status;

    private List<ProductCreateDTO> products;
    
}