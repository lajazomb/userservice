package bookstore.userservice.core.domain.service.interfaces;

import bookstore.userservice.core.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IUserService {

    public User createUser(User user);

    public User getUser(UUID user);

    public User getUserByUsername(String username);

    public User getUserByEmail(String email);

    public List<User> getUsers();

    public void updateUser(User product);

    public void removeUser(UUID id);

}
