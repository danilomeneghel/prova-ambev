package product.dto;

import lombok.Data;

@Data
public class ProductCreateDTO {

    private Long productNumber;
    
    private String name;
        
    private Double price;

}
