package serviceTests;

import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.UnauthorizedRequestException;
import model.UserData;
import model.requestresults.LoginRequest;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class UserServiceTests {

    @Test
    @DisplayName("Register User Pass")
    public void registerUserPass() throws BadRequestException, AlreadyTakenException {
        UserService userService = new UserService();
        Assertions.assertNotNull(userService.registerUser(new UserData("testuser", "password", "test@gmail.com")));
    }

    @Test
    @DisplayName("Register User Fail")
    public void registerUserFail() {
        UserService userService = new UserService();
        Assertions.assertThrows(BadRequestException.class, () -> userService.registerUser(new UserData(null, "password", "test@gmail.com")));
    }

    @Test
    @DisplayName("Login User Pass")
    public void loginUserPass() throws BadRequestException, AlreadyTakenException, UnauthorizedRequestException {
        UserService userService = new UserService();
        userService.registerUser(new UserData("testuser", "password", "test@gmail.com"));
        Assertions.assertNotNull(userService.loginUser(new LoginRequest("testuser", "password")));
    }

    @Test
    @DisplayName("Login User Fail")
    public void loginUserFail() throws BadRequestException, AlreadyTakenException {
        UserService userService = new UserService();
        userService.registerUser(new UserData("username", "password", "test@gmail.com"));
        Assertions.assertThrows(UnauthorizedRequestException.class, () -> userService.loginUser(new LoginRequest("username", "wrong")));
    }

    @Test
    @DisplayName("Logout User Pass")
    public void logoutUserPass() throws BadRequestException, AlreadyTakenException {
        UserService userService = new UserService();
        String authToken = userService.registerUser(new UserData("testuser", "password", "test@gmail.com"));
        Assertions.assertDoesNotThrow(() -> userService.logoutUser(authToken));
    }

    @Test
    @DisplayName("Logout User Fail")
    public void logoutUserFail() {
        UserService userService = new UserService();
        Assertions.assertThrows(UnauthorizedRequestException.class, () -> userService.logoutUser(null));
    }
}
