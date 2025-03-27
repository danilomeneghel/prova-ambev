package order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotNull(message = "Product Number cannot be null")
    @Column(nullable = false, unique = true)
    private Long productNumber;

    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    private String name = ""; 
    
    @NotNull(message = "Price cannot be null")
    @Column(nullable = false)
    private Double price;

}
