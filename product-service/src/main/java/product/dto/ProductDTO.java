package product.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private Long productNumber;
    
    private String name;
    
    private Double price;

}
