package nl.tudelft.oopp.group39.communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a quote from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getUsers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/user")).build();
        HttpResponse<String> response;
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

    private static String get(String serverRequest) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(serverRequest)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }

    public static String getRooms() throws IOException, InterruptedException {
        return get("http://localhost:8080/room");
    }

    public static String getBuildings() throws IOException, InterruptedException {
        return get("http://localhost:8080/building");
    }
}
