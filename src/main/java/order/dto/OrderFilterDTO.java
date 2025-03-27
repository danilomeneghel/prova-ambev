package order.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class OrderFilterDTO {

    private Long orderNumber;
    
    private LocalDate date;
    
    private String status;
    
    private Double totalValue;
    
}