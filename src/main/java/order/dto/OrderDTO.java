package order.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;
    
    private Long orderNumber;

    private Double totalValue;
    
    private LocalDateTime dateTime;

    private String status;
    
    private List<ProductDTO> products;

}