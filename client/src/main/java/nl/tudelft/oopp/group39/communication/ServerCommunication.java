package nl.tudelft.oopp.group39.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.controllers.MainSceneController;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    private static String url = "http://localhost:8080/";
    private static String user = "user/";
    private static String building = "building/";
    private static String room = "room/";
    private static String authenticate = "authenticate/";
    private static String facility = "facility/";

    /**
     * Retrieves users from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getUsers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + user)).build();
        return HttpRequest(request);
    }

    public static String getUser(String username) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + user + username)).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves buildings from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getBuildings() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + building)).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves rooms from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getRooms() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + room)).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves facilities from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getFacilities() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + facility)).build();
        return HttpRequest(request);
    }

    /**
     * Retrieves filtered list of buildings from the server
     *
     * @return the body of a get request to the server.
     */
    public static String getFilteredBuildings(String name, String location, String open, String closed, String capacity) {
        String urlString = url + "building?capacity=" + capacity + "&building=" + name + "&location=" + location + "&open=" + open + "&closed=" + closed;
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
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding).uri(URI.create(url + building)).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }

    /**
     * Adds a room on the server
     *
     * @return the body of a post request to the server.
     */
    public static String addRoom(String buildingId, String roomCapacity, String roomDescription) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\"" + roomCapacity + "\", \"description\":\"" + roomDescription + "\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding).uri(URI.create(url + room)).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }

    /**
     * Updates Rooms on the server
     *
     * @return the body of a put request to the server.
     */
    public static String updateRoom(String buildingId, String roomCapacity, String roomDescription, String id) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\"" + roomCapacity + "\", \"description\":\"" + roomDescription + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBuilding).uri(URI.create(url + room + id)).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }

    /**
     * Updates buildings on the server
     *
     * @return the body of a post request to the server.
     */
    public static String updateBuilding(String name, String location, String description, String open, String closed, String id) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers.ofString("{\"name\": \"" + name + "\", \"location\":\"" + location + "\", \"description\":\"" + description + "\", \"open\":\"" + open + "\", \"closed\":\"" + closed + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBuilding).uri(URI.create(url + building + id)).header("Content-Type", "application/json").build();
        return HttpRequest(request);
    }


    public static void removeBuilding(String id) {
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(url + building + id)).build();
        HttpRequest(request);
    }

    public static void removeRoom(String id) {
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(url + room + id)).build();
        HttpRequest(request);
    }

    public static String getRooms(long buildingId){
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + room + buildingId)).build();
        return HttpRequest(request);
    }

    public static String getAllRooms(){
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + room)).build();
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

    public static String addUser(String netID, String email, String password, String role){
        HttpRequest.BodyPublisher newUser = HttpRequest.BodyPublishers.ofString("{\"username\": \"" + netID + "\", \"email\":\"" + email + "\", \"password\":\"" + password + "\", \"roles\":\"" + List.of(role) + "\"}");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(newUser)
                .uri(URI.create(url + user))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 201) {
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
                .uri(URI.create(url + authenticate))
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
            System.out.println(response.body());
            JsonObject body = ((JsonObject) JsonParser.parseString(response.body()));
            String jwtToken = body.getAsJsonObject("body").get("token").getAsString();
            System.out.println(jwtToken);
            MainSceneController.jwt = jwtToken;
            MainSceneController.loggedIn = true;
            MainSceneController.username = username;
            //MainSceneController.isAdmin = isAdmin(username);
            return "Logged in";
        }
    }

    public static boolean isAdmin(String username) {
        String user = getUser(username);
        JsonObject body = ((JsonObject) JsonParser.parseString(user));
        System.out.println(body);
        String role = body.getAsJsonObject("body").get("role").getAsString();
        if(role.equals("admin")) return true;
        else return false;
    }
}
