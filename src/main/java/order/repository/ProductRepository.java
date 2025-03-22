package order.repository;

import order.dto.ProductCreateDTO;
import order.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT o FROM Product o WHERE " +
           "(:#{#productCreateDTO.productNumber} IS NULL OR o.productNumber = :#{#productCreateDTO.productNumber}) AND " +
           "(:#{#productCreateDTO.name} IS NULL OR o.name = :#{#productCreateDTO.name}) AND " +
           "(:#{#productCreateDTO.price} IS NULL OR o.price = :#{#productCreateDTO.price})")
    List<Product> findByCriteria(@Param("productCreateDTO") ProductCreateDTO productCreateDTO);
}
