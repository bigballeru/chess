package serverFacade;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.requestresults.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterAndLoginResult clearDb() throws ResponseException {
        var path = "/db";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("DELETE");

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, RegisterAndLoginResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public RegisterAndLoginResult registerUser(UserData userData) throws ResponseException {
        var path = "/user";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            writeBody(userData, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, RegisterAndLoginResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public RegisterAndLoginResult loginUser(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            writeBody(loginRequest, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, RegisterAndLoginResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest, AuthData authData) throws ResponseException {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            // To pass along AUTH code
            http.setRequestProperty("Authorization", authData.authToken());

            writeBody(createGameRequest, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, CreateGameResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public RegisterAndLoginResult logoutUser(AuthData authData) throws ResponseException {
        var path = "/session";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("DELETE");

            http.setRequestProperty("Authorization", authData.authToken());

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, RegisterAndLoginResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public ListGamesResult listGames(AuthData authData) throws ResponseException {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");

            http.setRequestProperty("Authorization", authData.authToken());

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, ListGamesResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public ListGamesResult joinGame(JoinGameRequest joinGameRequest, AuthData authData) throws ResponseException {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);

            http.setRequestProperty("Authorization", authData.authToken());

            writeBody(joinGameRequest, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, ListGamesResult.class);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
