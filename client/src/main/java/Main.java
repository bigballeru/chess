import model.AuthData;
import repls.PostLoginUI;
import repls.PreLoginUI;

public class Main {
    public static void main(String[] args) {
        PreLoginUI preLoginUI = new PreLoginUI();
        PostLoginUI postLoginUI = new PostLoginUI();

        while (true) {
            AuthData authData = preLoginUI.run();
            postLoginUI.run(authData);
            preLoginUI.logOut();
            postLoginUI.reset();
        }
    }
}
