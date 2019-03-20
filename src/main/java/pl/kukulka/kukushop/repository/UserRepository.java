package pl.kukulka.kukushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kukulka.kukushop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(long userId);

    User findByEmail(String email);

    User findByLogin(String login);

}
