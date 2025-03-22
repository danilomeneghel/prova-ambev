package order.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;
    
    private Long orderNumber;

    private Double totalValue;
    
    private LocalDate date;

    private String status;
    
    private List<ProductDTO> products;

}