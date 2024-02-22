package dataAccess;

public class UnauthorizedRequestException extends Exception {
    public UnauthorizedRequestException() {
        super("Error: unauthorized");
    }
}
