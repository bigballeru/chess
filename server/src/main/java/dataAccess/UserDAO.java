package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username);

    public void addUser(String username, String password, String email);

    public void clearAll();

    public boolean checkPassword(String username, String password);
}
