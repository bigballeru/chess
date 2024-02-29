package server;

import spark.*;

import java.nio.file.Paths;

public class Server {

    public static void main(String[] args) {
        Server newServer = new Server();
        newServer.run(8090);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", ServerHelper::register);
        Spark.post("/session", ServerHelper::login);
        Spark.post("/game", ServerHelper::createGame);
        Spark.delete("/db", ServerHelper::clear);
        Spark.delete("/session", ServerHelper::logout);
        Spark.get("/game", ServerHelper::listGames);
        Spark.put("/game", ServerHelper::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
