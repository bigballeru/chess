package dataAccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{
    @Override
    public UserData getUser(String username) {
        var statement = "SELECT username, password, email FROM users WHERE username=?";
    }

    @Override
    public void addUser(String username, String password, String email) {
        var statement = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
    }

    @Override
    public void clearAll() {
        var statement = "TRUNCATE users";
    }

    @Override
    public boolean checkPassword(String username, String password) {
        var statement = "SELECT password FROM users WHERE username=?";
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
