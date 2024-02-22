package server;

import dataAccess.BadRequestException;
import dataAccess.UnauthorizedRequestException;
import dataAccess.UsernameTakenException;
import model.requestresults.LoginRequest;
import model.requestresults.RegisterAndLoginResult;
import service.GameService;
import spark.*;
import com.google.gson.Gson;
import model.UserData;
import service.UserService;

public class ServerHelper {

    private static UserService userService = new UserService();
    private static GameService gameService = new GameService();

    public static Object register(Request request, Response response) {
        var registerRequest = new Gson().fromJson(request.body(), UserData.class);
        RegisterAndLoginResult toReturn = null;
        try {
            String code = userService.registerUser(registerRequest);
            toReturn = new RegisterAndLoginResult(registerRequest.username(), code, null);
            response.body(new Gson().toJson(toReturn));
            response.status(200);
        } catch (UsernameTakenException e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(403);
        } catch (BadRequestException e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(400);
        } catch (Exception e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(500);
        }
        return new Gson().toJson(toReturn);
    }

    public static Object login(Request request, Response response) {
        var loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        RegisterAndLoginResult toReturn = null;
        try {
            String code = userService.loginUser(loginRequest);
            toReturn = new RegisterAndLoginResult(loginRequest.username(), code, null);
            response.body(new Gson().toJson(toReturn));
            response.status(200);
        } catch (UnauthorizedRequestException e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(401);
        } catch (Exception e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(500);
        }
        return new Gson().toJson(toReturn);
    }

    public static Object createGame(Request request, Response response) {
        return null;
    }

    public static Object clear(Request request, Response response) {
        try {
            gameService.clearAll();
            userService.clearAll();
            response.status(200);
            return "{}";
        } catch (Exception e) {
            response.body(new Gson().toJson(e.getMessage()));
            RegisterAndLoginResult toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.status(500);
            return new Gson().toJson(toReturn);
        }
    }

    public static Object logout(Request request, Response response) {
        RegisterAndLoginResult toReturn = null;
        try {
            String authCode = request.headers("Authorization");
            userService.logoutUser(authCode);
            toReturn = new RegisterAndLoginResult(null, null, null);
            response.status(200);
        } catch (UnauthorizedRequestException e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(401);
        } catch (Exception e) {
            toReturn = new RegisterAndLoginResult(null, null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(500);
        }
        return new Gson().toJson(toReturn);
    }

    public static Object listGames(Request request, Response response) {
        return null;
    }

    public static Object joinGame(Request request, Response response) {
        return null;
    }
}