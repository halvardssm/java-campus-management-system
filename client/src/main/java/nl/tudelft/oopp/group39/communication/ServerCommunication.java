package nl.tudelft.oopp.group39.communication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();


    /**
     * Retrieves users from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getUsers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/user")).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves buildings from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getBuildings() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/building")).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves rooms from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getRooms() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/room")).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves facilities from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getFacilities() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/facility")).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves filtered list of buildings from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getFilteredBuildings(String name, String location, String open, String closed, String capacity) {
        String urlString = "http://localhost:8080/building?capacity=" + capacity + "&building=" + name + "&location=" + location + "&open=" + open + "&closed=" + closed;
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
        return HttpRequest(request);
    }

    /**
     * Adds a building on the server
     *
     * @return the body of a post request to the server.
     */
    public static String addBuilding(String name, String location, String description, String open, String closed) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"name\": \"" + name + "\", \"location\":\"" + location + "\", \"description\":\"" + description + "\", \"open\":\"" + open + "\", \"closed\":\"" + closed + "\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding).uri(URI.create("http://localhost:8080/building/")).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }

    /**
     * Adds a room on the server
     *
     * @return the body of a post request to the server.
     */
    public static String addRoom(String buildingId, String roomCapacity, String roomDescription) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\"" + roomCapacity + "\", \"description\":\"" + roomDescription + "\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding).uri(URI.create("http://localhost:8080/room/")).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }

    /**
     * Updates Rooms on the server
     *
     * @return the body of a put request to the server.
     */
    public static String updateRoom(String buildingId, String roomCapacity, String roomDescription, String id) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\"" + roomCapacity + "\", \"description\":\"" + roomDescription + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBuilding).uri(URI.create("http://localhost:8080/room/" + id)).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }

    /**
     * Updates buildings on the server
     *
     * @return the body of a post request to the server.
     */
    public static String updateBuilding(String name, String location, String description, String open, String closed, String id) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"name\": \"" + name + "\", \"location\":\"" + location + "\", \"description\":\"" + description + "\", \"open\":\"" + open + "\", \"closed\":\"" + closed + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBuilding).uri(URI.create("http://localhost:8080/building/" + id)).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }


    /**
     * @return the body of a get request to the server.
     */
    public static String HttpRequest(HttpRequest req) {
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
