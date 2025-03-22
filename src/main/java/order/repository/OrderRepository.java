package order.repository;

import order.dto.OrderCreateDTO;
import order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE " +
           "(:#{#orderCreateDTO.orderNumber} IS NULL OR o.orderNumber = :#{#orderCreateDTO.orderNumber}) AND " +
           "(:#{#orderCreateDTO.date} IS NULL OR o.date = :#{#orderCreateDTO.date}) AND " +
           "(:#{#orderCreateDTO.status} IS NULL OR o.status = :#{#orderCreateDTO.status})")
    List<Order> findByCriteria(@Param("orderCreateDTO") OrderCreateDTO orderCreateDTO);
}
