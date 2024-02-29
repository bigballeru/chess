package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData newAuth) throws DataAccessException;

    public void clearAll() throws DataAccessException;

    public boolean validateAuth(String myAuth) throws DataAccessException;

    public void deleteAuth(String myAuth) throws DataAccessException;

    public String getUsername(String myAuth) throws DataAccessException;
}
