package bookstore.userservice.core.domain.service.implementation;

import bookstore.userservice.core.domain.model.User;
import bookstore.userservice.core.domain.service.interfaces.IUserRepository;
import bookstore.userservice.core.domain.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository productRepository;

    @Override
    public User createUser(User user) {
        return productRepository.save(user);
    }

    @Override
    public User getUser(UUID user) {
        return productRepository.findById(user).orElseGet(null); // TODO: Throw exception?, don't pass null
    }

    @Override
    public User getUserByUsername(String username) {
        return productRepository.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return productRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return productRepository.findAll();
    }

    @Override
    public void updateUser(User user) {
        boolean exists = productRepository.findByUsername(user.getUsername()) != null;
        if (exists) {
            productRepository.deleteById(productRepository.findByUsername(user.getUsername()).getUserId());
            productRepository.save(user);
        }
    }

    @Override
    public void removeUser(UUID id) {
        productRepository.deleteById(id);
    }
}
