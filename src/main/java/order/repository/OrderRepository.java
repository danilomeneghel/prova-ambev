package order.repository;

import order.dto.OrderDTO;
import order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<OrderDTO> findByCriteria(OrderDTO orderDTO);
}
