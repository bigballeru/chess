package dataAccess;

import java.util.ArrayList;
import java.util.Objects;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

    final private ArrayList<UserData> myUsers = new ArrayList<>();

    @Override
    public UserData getUser(String username) {
        for (UserData user : myUsers) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null; //FIXME
    }

    @Override
    public void addUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        myUsers.add(newUser);
    }


    public void clearAll() {
        myUsers.clear();
    }

    @Override
    public boolean checkPassword(String username, String password) {
        for (UserData user : myUsers) {
            if (Objects.equals(user.username(), username)) {
                if (Objects.equals(user.password(), password)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }
}
