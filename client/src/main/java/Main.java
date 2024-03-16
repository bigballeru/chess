import chess.*;
import model.AuthData;
import repls.PostLoginUI;
import repls.PreLoginUI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class Main {
    public static void main(String[] args) {
        PreLoginUI preLoginUI = new PreLoginUI();
        PostLoginUI postLoginUI = new PostLoginUI();

        while (true) {
            AuthData authData = preLoginUI.run();
            postLoginUI.run(authData);
            preLoginUI.logOut();
        }
    }
}
