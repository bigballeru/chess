package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import model.requestresults.LoginRequest;

import java.util.UUID;

public class UserService {

    private static UserDAO userDAO = new MemoryUserDAO();
    private static AuthDAO authDAO = new MemoryAuthDAO();

    public void UserService() {
    }

    public String registerUser(UserData userData) throws UsernameTakenException, BadRequestException {
        if (userData.username() == null || userData.email() == null || userData.password() == null) {
            throw new BadRequestException();
        }
        if (userDAO.getUser(userData.username()) != null) {
            throw new UsernameTakenException();
        }
        userDAO.addUser(userData.username(), userData.password(), userData.email());
        String myUUID = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(myUUID, userData.username());
        authDAO.createAuth(newAuth);
        return myUUID;
    }

    public String loginUser(LoginRequest loginData) throws UnauthorizedRequestException {
        if (userDAO.getUser(loginData.username()) == null) {
            throw new UnauthorizedRequestException();
        }
        if (!userDAO.checkPassword(loginData.username(), loginData.password())) {
            throw new UnauthorizedRequestException();
        }
        String myUUID = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(myUUID, loginData.username());
        authDAO.createAuth(newAuth);
        return myUUID;
    }

    public void logoutUser(String authcode) throws UnauthorizedRequestException {
        if (!authDAO.validateAuth(authcode)) {
            throw new UnauthorizedRequestException();
        }
        authDAO.deleteAuth(authcode);
    }

    public void validateAuth(String authcode) throws UnauthorizedRequestException {
        if (!authDAO.validateAuth(authcode)) {
            throw new UnauthorizedRequestException();
        }
    }

    public void clearAll() {
        userDAO.clearAll();
        authDAO.clearAll();
    }

}
