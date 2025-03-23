package order.repository;

import order.dto.OrderFilterDTO;
import order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE " +
           "(:#{#orderFilterDTO.orderNumber} IS NULL OR o.orderNumber = :#{#orderFilterDTO.orderNumber}) AND " +
           "(:#{#orderFilterDTO.date} IS NULL OR o.date = :#{#orderFilterDTO.date}) AND " +
           "(:#{#orderFilterDTO.status} IS NULL OR o.status = :#{#orderFilterDTO.status})")
    List<Order> findByCriteria(@Param("orderFilterDTO") OrderFilterDTO orderFilterDTO);

    boolean existsByOrderNumber(Long orderNumber);
    
}
