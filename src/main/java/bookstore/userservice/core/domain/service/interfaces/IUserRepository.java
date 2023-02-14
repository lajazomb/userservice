package bookstore.userservice.core.domain.service.interfaces;

import bookstore.userservice.core.domain.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface IUserRepository extends CrudRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String username);

    //TODO: find a fix for findByUserId
    User findByUserId(UUID userId);

    List<User> findAll();

}
