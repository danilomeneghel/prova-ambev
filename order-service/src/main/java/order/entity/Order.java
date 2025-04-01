package order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NotNull(message = "Order Number cannot be null")
    @Column(nullable = false, unique = true)
    private Long orderNumber;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate date = LocalDate.now(); 
    
    @NotBlank(message = "Status cannot be null")
    @Column(nullable = false)
    private String status;

    private Double totalValue;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "orders_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
    
}
