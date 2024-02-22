package dataAccess;

public class UsernameTakenException extends Exception {
    public UsernameTakenException() {
        super("Error: already taken");
    }
}
