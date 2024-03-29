package dataAccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    final private ArrayList<AuthData> myAuths = new ArrayList<>();

    @Override
    public void createAuth(AuthData newAuth) {
        myAuths.add(newAuth);
    }

    /**
     *
     * @param myAuth
     * @return true if the auth token is valid, else false
     */
    public boolean validateAuth(String myAuth) {
        for (AuthData a : myAuths) {
            if (Objects.equals(a.authToken(), myAuth)) {
                return true;
            }
        }
        return false;
    }

    public void deleteAuth(String myAuth) {
        AuthData toDelete = null;
        for (AuthData a : myAuths) {
            if (Objects.equals(a.authToken(), myAuth)) {
                toDelete = a;
            }
        }
        if (toDelete != null) {
            myAuths.remove(toDelete);
        }
    }

    @Override
    public String getUsername(String myAuth) {
        for (AuthData a : myAuths) {
            if (Objects.equals(a.authToken(), myAuth)) {
                return a.username();
            }
        }
        return ""; //SHOULD NEVER RETURN THIS
    }

    public void clearAll() {
        myAuths.clear();
    }
}
