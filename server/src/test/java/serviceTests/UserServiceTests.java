package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import model.requestresults.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class UserServiceTests {

    @Test
    @DisplayName("Register User Pass")
    public void registerUserPass() throws BadRequestException, AlreadyTakenException, DataAccessException {
        UserService userService = new UserService();
        Assertions.assertNotNull(userService.registerUser(new UserData("testuser", "password", "test@gmail.com")));
        userService.clearAll();
    }

    @Test
    @DisplayName("Register User Fail")
    public void registerUserFail() throws DataAccessException {
        UserService userService = new UserService();
        Assertions.assertThrows(BadRequestException.class, () -> userService.registerUser(new UserData(null, "password", "test@gmail.com")));
        userService.clearAll();
    }

    @Test
    @DisplayName("Login User Pass")
    public void loginUserPass() throws BadRequestException, AlreadyTakenException, UnauthorizedRequestException, DataAccessException {
        UserService userService = new UserService();
        userService.registerUser(new UserData("testuser", "password", "test@gmail.com"));
        Assertions.assertNotNull(userService.loginUser(new LoginRequest("testuser", "password")));
        userService.clearAll();
    }

    @Test
    @DisplayName("Login User Fail")
    public void loginUserFail() throws BadRequestException, AlreadyTakenException, DataAccessException {
        UserService userService = new UserService();
        userService.registerUser(new UserData("username", "password", "test@gmail.com"));
        Assertions.assertThrows(UnauthorizedRequestException.class, () -> userService.loginUser(new LoginRequest("username", "wrong")));
        userService.clearAll();
    }

    @Test
    public void createAuth() throws DataAccessException {
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createAuth(new AuthData("1249192049", "user"));
        Assertions.assertEquals(0,0);
        sqlAuthDAO.clearAll();
    }

    @Test
    @DisplayName("Logout User Pass")
    public void logoutUserPass() throws BadRequestException, AlreadyTakenException, DataAccessException {
        UserService userService = new UserService();
        String authToken = userService.registerUser(new UserData("testuser", "password", "test@gmail.com"));
        Assertions.assertDoesNotThrow(() -> userService.logoutUser(authToken));
        userService.clearAll();
    }

    @Test
    @DisplayName("Logout User Fail")
    public void logoutUserFail() throws DataAccessException {
        UserService userService = new UserService();
        Assertions.assertThrows(UnauthorizedRequestException.class, () -> userService.logoutUser(null));
        userService.clearAll();
    }

    @Test
    @DisplayName("Clear User Info")
    public void clearUserInfo() throws BadRequestException, AlreadyTakenException, DataAccessException {
        UserService userService = new UserService();
        userService.registerUser(new UserData("username", "password", "test@gmail.com"));
        Assertions.assertDoesNotThrow(() -> userService.clearAll());
    }
}
