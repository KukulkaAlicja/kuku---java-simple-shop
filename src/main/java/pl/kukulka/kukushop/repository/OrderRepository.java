package pl.kukulka.kukushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kukulka.kukushop.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserLogin(String login);

}
