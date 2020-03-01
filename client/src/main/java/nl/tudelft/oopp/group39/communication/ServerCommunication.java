package nl.tudelft.oopp.group39.communication;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a quote from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getUsers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/user")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }

    public static String addUser(String netID, String email, String password){
        HttpRequest.BodyPublisher user = HttpRequest.BodyPublishers.ofString("{\"id\": \"" + netID + "\", \"email\":\"" + email + "\", \"password\":\"" + password + "\"}");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(user)
                .uri(URI.create("http://localhost:8080/user"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return "Something went wrong";
        }
        else{
            return "Account created";
        }

    }

    public static String userLogin(String username, String pwd){
        HttpRequest.BodyPublisher user = HttpRequest.BodyPublishers.ofString("{\"username\": \"" + username + "\", \"password\":\"" + pwd + "\"}");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(user)
                .uri(URI.create("http://localhost:8080/authenticate"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return "Something went wrong";
        }
        else{
            return "Logged in";
        }
    }
}
