package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;

    public void addUser(String username, String password, String email) throws DataAccessException;

    public void clearAll() throws DataAccessException;

    public boolean checkPassword(String username, String password) throws DataAccessException;
}
