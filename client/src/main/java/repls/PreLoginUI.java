package repls;

import java.util.Arrays;
import java.util.Scanner;

import model.AuthData;
import model.UserData;
import model.requestresults.LoginRequest;
import model.requestresults.RegisterAndLoginResult;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import static ui.EscapeSequences.*;

public class PreLoginUI {

    private String username = null;
    private String authToken = null;
    private String url = "http://localhost:8080";
    private State state = State.SIGNEDOUT;

    public AuthData run() {
        System.out.println("\uD83D\uDC51 Welcome to chess :). Type help to get started. \uD83D\uDC51");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("  quit") && (state == State.SIGNEDOUT)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

        System.out.println();

        if (result.equals("  quit")) {
            System.exit(0);
        }

        return new AuthData(authToken, username);
    }

    public void logOut() {
        state = State.SIGNEDOUT;
        username = null;
        authToken = null;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "  quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            username = params[0];
            String password = params[1];
            String email = params[2];

            ServerFacade serverFacade = new ServerFacade(url);
            try {
                RegisterAndLoginResult result = serverFacade.registerUser(new UserData(username, password, email));
                authToken = result.authToken();
                state = State.SIGNEDIN;
                return String.format("  You created an account with the username %s.", username);
            } catch (Exception e) {
                throw e;
            }
        }
        throw new ResponseException(400, "  Need all three: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            username = params[0];
            String password = params[1];

            ServerFacade serverFacade = new ServerFacade(url);
            try {
                RegisterAndLoginResult result = serverFacade.loginUser(new LoginRequest(username, password));
                authToken = result.authToken();
                state = State.SIGNEDIN;
                return String.format("  You logged in with the account under username %s.", username);
            } catch (Exception e) {
                throw e;
            }
        }
        throw new ResponseException(400, "  Need both <USERNAME> <PASSWORD>");
    }

    public String help() {
        return """
                 register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                 login <USERNAME> <PASSWORD> - to login to your account
                 quit - to quit playing chess
                 help - shows possible commands
               """;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[LOGGED_OUT] >>> " + SET_TEXT_COLOR_GREEN);
    }
}
