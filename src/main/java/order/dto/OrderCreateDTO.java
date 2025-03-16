package order.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreateDTO {

    private List<ProductCreateDTO> products;

}