package order.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;
    
    private Long orderNumber;
    
    private LocalDate date;

    private String status;
    
    private Double totalValue;

    private List<ProductDTO> products = new ArrayList<>(); 
    
}
