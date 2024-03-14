package clientTests;

import model.UserData;
import model.requestresults.RegisterAndLoginResult;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.*;

public class ServerFacadeTests {

    private static Server server;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearDb();
        server.stop();
    }

    @Test
    @DisplayName("Register User Test Pass")
    public void registerUserTestPass() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        RegisterAndLoginResult authCode = serverFacade.registerUser(new UserData("user","password","email"));
        Assertions.assertTrue(authCode.authToken() != null);
    }

    @Test
    @DisplayName("Register User Test Fail")
    public void registerUserTestFail() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + port);
        Assertions.assertThrows(Exception.class, () -> serverFacade.registerUser(new UserData(null, null, null)));
    }
}