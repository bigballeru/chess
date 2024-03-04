package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {

    @BeforeEach
    public void setUp() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.clearAll();
    }

    @Test
    @DisplayName("Create Auth Test Past")
    public void createAuthTestPass() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        Assertions.assertDoesNotThrow(() -> sqlAuthDAO.createAuth(new AuthData("129418-af78971-as87891", "username")));
    }

    @Test
    @DisplayName("Create Auth Test Fail")
    public void createAuthTestFail() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        Assertions.assertThrows(DataAccessException.class, () -> sqlAuthDAO.createAuth(new AuthData(null, null)));
    }

    @Test
    @DisplayName("Clear All Test Pass")
    public void clearAllTestPass() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("12345","user"));
        Assertions.assertDoesNotThrow(() -> sqlAuthDAO.clearAll());
    }

    @Test
    @DisplayName("Validate Auth Test Pass")
    public void validateAuthTestPass() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("12345", "user"));
        Assertions.assertDoesNotThrow(() -> sqlAuthDAO.validateAuth("12345"));
    }

    @Test
    @DisplayName("Validate Auth Test Fail")
    public void validateAuthTestFail() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("12345", "user"));
        Assertions.assertFalse(() -> {
            try {
                return sqlAuthDAO.validateAuth("54321");
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("Delete Auth Test Pass 1")
    public void deleteAuthTestPass1() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("12345", "user"));
        sqlAuthDAO.deleteAuth("12345");
        Assertions.assertFalse(() -> {
            try {
                return sqlAuthDAO.validateAuth("12345");
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("Delete Auth Test Pass 2")
    public void deleteAuthTestPass2() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("14512", "user"));
        sqlAuthDAO.deleteAuth("14512");
        Assertions.assertFalse(() -> {
            try {
                return sqlAuthDAO.validateAuth("14512");
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("Get Username Test Pass 1")
    public void getUsernameTestPass1() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("12345","user"));
        Assertions.assertEquals("user", sqlAuthDAO.getUsername("12345"));
    }

    @Test
    @DisplayName("Get Username Test Pass 2")
    public void getUsernameTestPass2() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("72812","lover"));
        Assertions.assertEquals("lover", sqlAuthDAO.getUsername("72812"));
    }
}
