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

    public String registerUser(UserData userData) throws AlreadyTakenException, BadRequestException {
        if (userData.username() == null || userData.email() == null || userData.password() == null) {
            throw new BadRequestException();
        }
        if (userDAO.getUser(userData.username()) != null) {
            throw new AlreadyTakenException();
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

    public void logoutUser(String authCode) throws UnauthorizedRequestException {
        if (!authDAO.validateAuth(authCode)) {
            throw new UnauthorizedRequestException();
        }
        authDAO.deleteAuth(authCode);
    }

    public void validateAuth(String authCode) throws UnauthorizedRequestException {
        if (!authDAO.validateAuth(authCode)) {
            throw new UnauthorizedRequestException();
        }
    }

    public String getUsername(String authCode) {
        return authDAO.getUsername(authCode);
    }

    public void clearAll() {
        userDAO.clearAll();
        authDAO.clearAll();
    }

}
