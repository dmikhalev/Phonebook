package backend.service;

import backend.entity.User;
import backend.jdbc.UserDAO;

import java.util.List;

public class UserService {

    private User user;
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean createUser(String username, String password, String telephoneNumber, String email) {
        User user = new User(null, username, password, telephoneNumber, email);
        return userDAO.create(user) > 0;
    }

    public boolean login(String username, String password) {
        User user = userDAO.readByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            this.user = user;
            return true;
        }
        return false;
    }

    public void logOut() {
        user = null;
    }

    public boolean isLogged() {
        return user != null;
    }

    public boolean editUser(User user) {
        if (userDAO.update(user)) {
            this.user = user;
            return true;
        }
        return false;
    }

    public User getUser() {
        return user;
    }

    public boolean deleteByCredentials(String username, String password) {
        return userDAO.deleteByCredentials(username, password);
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }
}
