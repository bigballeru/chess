package repls;

import model.AuthData;
import model.UserData;
import model.requestresults.*;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLoginUI {

    private AuthData authData = null;
    private String url = "http://localhost:8080";
    private State state = State.SIGNEDIN;

    public void run(AuthData authData) {
        this.authData = authData;

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("  quit") && (state == State.SIGNEDIN)) {
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
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> join(params);
                case "logout" -> logout();
                case "quit" -> "  quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length == 1) {
            String gamename = params[0];

            ServerFacade serverFacade = new ServerFacade(url);
            try {
                CreateGameResult createGameResult = serverFacade.createGame(new CreateGameRequest(gamename), authData);
                return String.format("  You created a game with the name %s.", gamename);
            } catch (Exception e) {
                throw e;
            }
        }
        throw new ResponseException(400, "  Need <NAME>");
    }

    public String list() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade(url);
        try {
            ListGamesResult listGamesResult = serverFacade.listGames(authData);
            return String.format("  Here are the current games %s.", listGamesResult.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    public String join(String... params) throws ResponseException {
        if (params.length == 1) {
            ServerFacade serverFacade = new ServerFacade(url);
            Integer gameID = Integer.parseInt(params[0]);

            serverFacade.joinGame(new JoinGameRequest(null,gameID), authData);
            return String.format(" You are now watching game %s", params[0]);
        }
        if (params.length == 2) {
            ServerFacade serverFacade = new ServerFacade(url);
            Integer gameID = Integer.parseInt(params[0]);
            String color = params[1];

            serverFacade.joinGame(new JoinGameRequest(color, gameID), authData);
            return String.format(" You successfully joined game %s", params[0]);
        }
        throw new ResponseException(400, " Need <ID> [WHITE]");
    }

    public String logout() throws ResponseException {
        ServerFacade serverFacade = new ServerFacade(url);
        try {
            serverFacade.logoutUser(authData);
            state = State.SIGNEDOUT;
            return String.format("  You have successfully logged out.");
        } catch (Exception e) {
            throw e;
        }
    }

    public String help() {
        return """
                 create <NAME> - to create a game
                 list - to list games
                 join <ID> [WHITE|BLACK|<empty>] - to join a game
                 observe <ID> - watch a game
                 logout - to logout when you are done
                 quit - to quit playing chess
                 help - to see all commands
               """;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[LOGGED_IN] >>> " + SET_TEXT_COLOR_GREEN);
    }
}
