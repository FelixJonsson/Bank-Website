package isp.eao;

import jakarta.ejb.Local;
import java.util.List;
import isp.entity.User;

@Local
public interface UserEAOLocal {
    public User findUser(int id);
    public User createUser(User user);
    public User updateUser(User user);
    public void deleteUser(int id);
    public User findUserByEmail(String email);
    public List<User> getAllUsers();
}