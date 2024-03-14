package server;

import dataAccess.BadRequestException;
import dataAccess.UnauthorizedRequestException;
import dataAccess.AlreadyTakenException;
import model.requestresults.*;
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
        } catch (AlreadyTakenException e) {
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
        var createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
        String authCode = request.headers("Authorization");
        CreateGameResult toReturn = null;
        try {
            userService.validateAuth(authCode);
            toReturn = new CreateGameResult(gameService.createGame(createGameRequest), null);
            response.status(200);
        } catch (UnauthorizedRequestException e) {
            toReturn = new CreateGameResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(401);
        } catch (BadRequestException e) {
            toReturn = new CreateGameResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(400);
        } catch (Exception e) {
            toReturn = new CreateGameResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(500);
        }
        return new Gson().toJson(toReturn);
    }

    public static Object clear(Request request, Response response) {
        try {
            gameService.clearAll();
            userService.clearAll();
            response.status(200);
            RegisterAndLoginResult toReturn = new RegisterAndLoginResult(null, null, null);
            return new Gson().toJson(toReturn);
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
        String authCode = request.headers("Authorization");
        ListGamesResult toReturn = null;
        try {
            userService.validateAuth(authCode);
            toReturn = new ListGamesResult(gameService.listGames(), null);
            response.body(new Gson().toJson(toReturn));
            response.status(200);
        } catch (UnauthorizedRequestException e) {
            toReturn = new ListGamesResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(401);
        } catch (Exception e) {
            toReturn = new ListGamesResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(500);
        }
        return new Gson().toJson(toReturn);
    }

    public static Object joinGame(Request request, Response response) {
        String authCode = request.headers("Authorization");
        var joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        ListGamesResult toReturn = new ListGamesResult(null, null);
        try {
            userService.validateAuth(authCode);
            String myUsername = userService.getUsername(authCode);
            gameService.joinGame(joinGameRequest, myUsername);
            response.status(200);
        } catch (UnauthorizedRequestException e) {
            toReturn = new ListGamesResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(401);
        } catch (BadRequestException e) {
            toReturn = new ListGamesResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(400);
        } catch (AlreadyTakenException e) {
            toReturn = new ListGamesResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(403);
        } catch (Exception e) {
            toReturn = new ListGamesResult(null, e.getMessage());
            response.body(new Gson().toJson(e.getMessage()));
            response.status(500);
        }
        return new Gson().toJson(toReturn);
    }
}