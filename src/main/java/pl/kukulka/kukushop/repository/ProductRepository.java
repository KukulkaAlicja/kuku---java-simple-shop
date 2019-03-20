package pl.kukulka.kukushop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kukulka.kukushop.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductId(long productId);

    List<Product> findByUserLogin(String login);

    List<Product> findAllByAvailable(boolean b);

    Page<Product> findAllByAvailableTrue(Pageable b);

}
