package nl.tudelft.oopp.demo.communication;

import netscape.javascript.JSObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a quote from the server.
     * @return the body of a get request to the server.
     * @throws Exception if communication with the server fails.
     */
    public static String getQuote() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/quote")).build();
        return GenData(request);
    }

    //IMPORTANT FOR SUNDAY
    public static String getUsers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/user")).build();
        return GenData(request);
    }

    public static String getBuilding() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/building")).build();
        return GenData(request);
    }
    public static String getRoom() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/room")).build();
        return GenData(request);
    }

    public static String getFilteredBuildings(String name, String location){//, LocalTime open, LocalTime closed) {
        String urlString = "http://localhost:8080/building/filter?capacity=10&building="+name+"&location="+location;//+"&open="+open+"&closed="+closed;
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
        return GenData(request);
    }

    public static String addBuilding(String name, String location, String description) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"name\": \""+name+ "\", \"location\":\""+ location+"\", \"description\":\""+ description +"\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding).uri(URI.create("http://localhost:8080/building")).header("Content-Type", "application/json").build();
        return GenData(request);
    }

    public static String GenData(HttpRequest req) {
        HttpRequest request = req;
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

}
