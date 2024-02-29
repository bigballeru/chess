package dataAccess;

import com.google.gson.Gson;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws Exception {

    }

    @Override
    public void createAuth(AuthData newAuth) {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        var json = new Gson().toJson(newAuth);
    }

    @Override
    public void clearAll() {
        var statement = "TRUNCATE auth";
    }

    @Override
    public boolean validateAuth(String myAuth) {
        var statement = "SELECT authToken FROM auth WHERE authToken=?";
    }

    @Override
    public void deleteAuth(String myAuth) {
        var statement = "DELETE FROM auth WHERE authToken=?";
    }

    @Override
    public String getUsername(String myAuth) {
        var statement = "SELECT username FROM auth WHERE authToken=?";
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                `username` varchar(256) NOT NULL,
                `authToken` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
                INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
