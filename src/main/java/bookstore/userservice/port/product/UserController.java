package bookstore.userservice.port.product;

import bookstore.userservice.core.domain.model.User;
import bookstore.userservice.core.domain.service.implementation.UserService;
import bookstore.userservice.port.product.exception.EmptySearchResultException;
import bookstore.userservice.port.product.exception.NoUsersException;
import bookstore.userservice.port.product.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() throws NoUsersException {
        List<User> users = userService.getUsers();

        if (users == null || users.size() == 0) {
            throw new NoUsersException();
        }

        return users;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable UUID id) throws UserNotFoundException {
        User user = userService.getUser(id);

        if (user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }

    @GetMapping("/users/{string}")
    public User getUser(@PathVariable String string) throws UserNotFoundException {
        // TODO: remove code duplicate
        boolean email = string.contains("@");
        User user;
        if (email) { // TODO: Implement regex parsing for emails maybe
            user = userService.getUserByEmail(string);
        }else {
            user = userService.getUserByUsername(string);
        }

        if (user == null) {
            throw new UserNotFoundException(email, string);
        }

        return user;
    }

    //TODO: Find a way to test in US
    @PostMapping("/users")
    public @ResponseBody User createProduct (@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.removeUser(id);
    }

    //TODO: Find a way to test in US
    @PutMapping(path="/users")
    public void update (@RequestBody User user) {
        userService.updateUser(user);
    }

}
