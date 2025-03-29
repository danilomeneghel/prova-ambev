package order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import order.dto.OrderFilterDTO;
import order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE " +
           "(:#{#orderFilterDTO.orderNumber} IS NULL OR o.orderNumber = :#{#orderFilterDTO.orderNumber}) AND " +
           "(:#{#orderFilterDTO.date} IS NULL OR o.date = :#{#orderFilterDTO.date}) AND " +
           "(:#{#orderFilterDTO.status} IS NULL OR o.status = :#{#orderFilterDTO.status}) AND " +
           "(:#{#orderFilterDTO.totalValue} IS NULL OR o.totalValue = CAST(:#{#orderFilterDTO.totalValue} AS double))")
    List<Order> findByCriteria(@Param("orderFilterDTO") OrderFilterDTO orderFilterDTO);

    boolean existsByOrderNumber(Long orderNumber);
}
