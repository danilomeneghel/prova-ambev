package order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import order.dto.ProductCreateDTO;
import order.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT o FROM Product o WHERE " +
           "(:#{#productCreateDTO.productNumber} IS NULL OR o.productNumber = :#{#productCreateDTO.productNumber}) AND " +
           "(:#{#productCreateDTO.name} IS NULL OR o.name = :#{#productCreateDTO.name}) AND " +
           "(:#{#productCreateDTO.price} IS NULL OR o.price = CAST(:#{#productCreateDTO.price} AS double))")
    List<Product> findByCriteria(@Param("productCreateDTO") ProductCreateDTO productCreateDTO);

    boolean existsByProductNumber(Long productNumber);

    Optional<Product> findByProductNumber(Long productNumber);
    
}
