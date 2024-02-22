package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData newAuth);

    public void clearAll();

    public boolean validateAuth(String myAuth);

    public void deleteAuth(String myAuth);
}
