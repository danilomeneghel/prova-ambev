package order.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderCreateDTO {

    private Long orderNumber;
    
    private LocalDate date;
    
    private String status;

    private List<ProductCreateDTO> products;
    
}