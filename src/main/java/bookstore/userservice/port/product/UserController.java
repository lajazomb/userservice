package bookstore.userservice.port.product;

import bookstore.userservice.core.domain.model.User;
import bookstore.userservice.core.domain.service.implementation.UserService;
import bookstore.userservice.port.product.exception.*;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable UUID id) throws UserNotFoundException {
        User user = userService.getUser(id);

        if (user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }

    @GetMapping("/users/{string:.+}/")
    public User getUserByString(@PathVariable(value="string") String string, HttpServletResponse response) throws UserNotFoundException {
        response.setContentType("application/x-www-form-urlencoded"); // avoid issues with dots and other special chars in request

        User user;
        boolean email = string.contains("@"); // TODO: proper email parsing

        if (email) {
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
    public @ResponseBody User createUser (@RequestBody User user) throws UserUsernameAlreadyExistsException, UserEmailAlreadyExistsException {
        String username = user.getUsername();
        String email = user.getEmail();

        // Check if username already exists
        if (userService.getUserByUsername(username) != null) {
            throw new UserUsernameAlreadyExistsException(username);
        }

        // Check if email already exists
        if (userService.getUserByEmail(email) != null) {
            throw new UserEmailAlreadyExistsException(email);
        }

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
