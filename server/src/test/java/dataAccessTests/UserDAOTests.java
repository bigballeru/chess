package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserDAOTests {
    @BeforeEach
    public void setUp() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.clearAll();
    }

    @Test
    @DisplayName("Get User Test Pass 1")
    public void getUserTestPass1() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.addUser("user","password","email");
        var checkUser = new UserData("user", "password", "email");
        var returnedUser = sqlUserDAO.getUser("user");
        Assertions.assertEquals(checkUser.username(), returnedUser.username());
        Assertions.assertEquals(checkUser.email(), returnedUser.email());
    }

    @Test
    @DisplayName("Get User Test Pass 2")
    public void getUserTestPass2() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.addUser("bob","password","bobby");
        var checkUser = new UserData("bob", "password", "bobby");
        var returnedUser = sqlUserDAO.getUser("bob");
        Assertions.assertEquals(checkUser.username(), returnedUser.username());
        Assertions.assertEquals(checkUser.email(), returnedUser.email());
    }

    @Test
    @DisplayName("Add User Test Pass")
    public void addUserTestPass() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        Assertions.assertDoesNotThrow(() -> sqlUserDAO.addUser("username", "password", "email"));
    }

    @Test
    @DisplayName("Add User Test Fail")
    public void addUserTestFail() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        Assertions.assertThrows(Exception.class, () -> sqlUserDAO.addUser(null, null, null));
    }

    @Test
    @DisplayName("Clear All Test Pass")
    public void clearAllTestPass() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.addUser("user","password","email");
        Assertions.assertDoesNotThrow(() -> sqlUserDAO.clearAll());
    }

    @Test
    @DisplayName("Check Password Test Pass 1")
    public void checkPasswordTestPass1() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.addUser("username","password","email");
        Assertions.assertTrue(sqlUserDAO.checkPassword("username","password"));
    }

    @Test
    @DisplayName("Check Password Test Pass 2")
    public void checkPasswordTestPass2() throws DataAccessException {
        SQLUserDAO sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.addUser("username","passwor","email");
        Assertions.assertFalse(sqlUserDAO.checkPassword("username","password"));
    }
}
