package clientTests;

import model.AuthData;
import model.UserData;
import model.requestresults.*;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.*;

public class ServerFacadeTests {

    private static Server server;
    private static int port;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        server.stop();
    }

    @Test
    @DisplayName("Register User Test Pass")
    public void registerUserTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        Assertions.assertTrue(authCode.authToken() != null);
    }

    @Test
    @DisplayName("Register User Test Fail")
    public void registerUserTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        Assertions.assertThrows(Exception.class, () -> serverFacade.registerUser(new UserData(null, null, null)));
    }

    @Test
    @DisplayName("Login User Test Pass")
    public void loginUserTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        serverFacade.logoutUser(new AuthData(authCode.authToken(),"user"));
        Assertions.assertNotNull(serverFacade.loginUser(new LoginRequest("user", "password")));
    }

    @Test
    @DisplayName("Login User Test Fail")
    public void loginUserTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        Assertions.assertThrows(Exception.class, () -> serverFacade.loginUser(new LoginRequest(null, null)));
    }

    @Test
    @DisplayName("Create Game Test Pass")
    public void createGameTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(new CreateGameRequest("test"), new AuthData(authCode.authToken(), "user")));
    }

    @Test
    @DisplayName("Create Game Test Fail")
    public void createGameTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        Assertions.assertThrows(Exception.class, () -> serverFacade.createGame(new CreateGameRequest("test"), new AuthData(null, "user")));
    }

    @Test
    @DisplayName("Logout User Test Pass")
    public void logoutUserTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.logoutUser(new AuthData(authCode.authToken(),"user")));
    }

    @Test
    @DisplayName("Logout User Test Fail")
    public void logoutUserTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        Assertions.assertThrows(Exception.class, () -> serverFacade.logoutUser(new AuthData(null,"user")));
    }

    @Test
    @DisplayName("List Games Test Pass")
    public void listGamesTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        serverFacade.createGame(new CreateGameRequest("test1"), new AuthData(authCode.authToken(), "user"));
        serverFacade.createGame(new CreateGameRequest("test2"), new AuthData(authCode.authToken(), "user"));
        serverFacade.createGame(new CreateGameRequest("test3"), new AuthData(authCode.authToken(), "user"));
        ListGamesResult listGamesResult = serverFacade.listGames(new AuthData(authCode.authToken(), "user"));
        Assertions.assertEquals(3, listGamesResult.games().size());
    }

    @Test
    @DisplayName("List Games Test Fail")
    public void listGamesTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        serverFacade.createGame(new CreateGameRequest("test1"), new AuthData(authCode.authToken(), "user"));
        Assertions.assertThrows(Exception.class, () -> serverFacade.listGames(new AuthData(null, "user")));
    }

    @Test
    @DisplayName("Join Game Test Pass")
    public void joinGameTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user", "password", "email"));
        CreateGameResult gameID = serverFacade.createGame(new CreateGameRequest("test1"), new AuthData(authCode.authToken(), "user"));
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(new JoinGameRequest("black", gameID.gameID()), new AuthData(authCode.authToken(), "user")));
    }

    @Test
    @DisplayName("Join Game Test Fail")
    public void joinGameTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user", "password", "email"));
        CreateGameResult gameID = serverFacade.createGame(new CreateGameRequest("test1"), new AuthData(authCode.authToken(), "user"));
        Assertions.assertThrows(Exception.class, () -> serverFacade.joinGame(new JoinGameRequest("BLACK", gameID.gameID()), new AuthData(null, "user")));
    }

    @Test
    @DisplayName("Clear DB Test Pass")
    public void clearDBTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user", "password", "email"));
        CreateGameResult gameID = serverFacade.createGame(new CreateGameRequest("test1"), new AuthData(authCode.authToken(), "user"));
        Assertions.assertDoesNotThrow(() -> serverFacade.clearDb());
    }
}