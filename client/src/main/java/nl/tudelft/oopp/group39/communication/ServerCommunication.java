package nl.tudelft.oopp.group39.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.User;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    private static String url = "http://localhost:8080/";
    public static String user = "user/";
    public static String building = "building/";
    public static String room = "room/";
    public static String authenticate = "authenticate/";
    public static String facility = "facility/";

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Retrieves all Objects of type type.
     *
     * @param type the type of objects we want to retrieve
     * @return the body of a get request to the server.
     */
    public static String get(String type) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + type)).build();
        return httpRequest(request);
    }

    /**
     * Retrieves user from the server based on username.
     *
     * @param username username of the user that needs to be retrieved
     * @return the body of a get request to the server.
     */
    public static User getUser(String username) throws JsonProcessingException {
        HttpRequest request =
            HttpRequest.newBuilder().GET().uri(URI.create(url + user + username)).build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode userJson = mapper.readTree(httpRequest(request)).get("body");
        String userAsString = mapper.writeValueAsString(userJson);
        return mapper.readValue(userAsString, User.class);
    }

    /**
     * Retrieves rooms from the server based on building id.
     *
     * @param buildingId id of the building
     * @return the body of a get request to the server.
     */
    public static String getRooms(long buildingId) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url + "room?buildingId=" + buildingId))
            .build();
        return httpRequest(request);
    }

    /**
     * Retrieves filtered list of buildings from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getFilteredBuildings(
        String name,
        String location,
        String open,
        String closed,
        String capacity
    ) {
        String urlString = url + "building?capacity=" + capacity + "&building=" + name
            + "&location=" + location + "&open=" + open + "&closed=" + closed;
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
        return httpRequest(request);
    }

    /**
     * Adds a building on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String addBuilding(
        String name, String location,
        String description,
        String open,
        String closed
    ) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers
            .ofString("{\"name\": \"" + name + "\", \"location\":\"" + location
                + "\", \"description\":\"" + description + "\", \"open\":\"" + open
                + "\", \"closed\":\"" + closed + "\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding)
            .uri(URI.create(url + "building/"))
            .header("Content-Type", "application/json").build();
        return httpRequest(request);
    }

    /**
     * Adds a room on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String addRoom(String buildingId, String roomCapacity, String roomDescription) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers
            .ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\""
                + roomCapacity + "\", \"description\":\"" + roomDescription + "\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBuilding)
            .uri(URI.create(url + "room/"))
            .header("Content-Type", "application/json").build();
        return httpRequest(request);
    }

    /**
     * Updates Rooms on the server.
     *
     * @return the body of a put request to the server.
     */
    public static String updateRoom(
        String buildingId,
        String roomCapacity,
        String roomDescription,
        String id
    ) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers
            .ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\""
                + roomCapacity + "\", \"description\":\"" + roomDescription + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBuilding)
            .uri(URI.create(url + "room/" + id))
            .header("Content-Type", "application/json").build();
        return httpRequest(request);
    }

    /**
     * Updates buildings on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String updateBuilding(
        String name,
        String location,
        String description,
        String open,
        String closed,
        String id
    ) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers
            .ofString("{\"name\": \"" + name + "\", \"location\":\"" + location
                + "\", \"description\":\"" + description + "\", \"open\":\"" + open
                + "\", \"closed\":\"" + closed + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBuilding)
            .uri(URI.create(url + "building/" + id))
            .header("Content-Type", "application/json").build();
        return httpRequest(request);
    }

    /**
     * Doc. TODO Sven
     */
    public static void removeBuilding(String id) {
        HttpRequest request = HttpRequest.newBuilder().DELETE()
            .uri(URI.create(url + "building/" + id)).build();
        httpRequest(request);
    }

    /**
     * Doc. TODO Sven
     */
    public static void removeRoom(String id) {
        HttpRequest request = HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(url + "room/" + id))
            .build();
        httpRequest(request);
    }

    /**
     * Doc. TODO Sven
     *
     * @return the body of a get request to the server.
     */
    public static String httpRequest(HttpRequest req) {
        HttpResponse<String> response;
        try {
            response = client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Creates user on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String addUser(User newUser) throws JsonProcessingException {
        String userJson = mapper.writeValueAsString(newUser);
        HttpRequest.BodyPublisher signup = HttpRequest.BodyPublishers
            .ofString(userJson);
        HttpRequest request = HttpRequest.newBuilder()
            .POST(signup)
            .uri(URI.create(url + user))
            .header("Content-Type", "application/json")
            .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 201) {
            System.out.println("Status: " + response.statusCode());
            return "Something went wrong";
        } else {
            return "Account created";
        }

    }

    /**
     * User login.
     *
     * @return the body of a post request to the server.
     */
    public static String userLogin(String username, String pwd) throws JsonProcessingException {
        HttpRequest.BodyPublisher user = HttpRequest.BodyPublishers
            .ofString("{\"username\": \"" + username
                + "\", \"password\":\"" + pwd + "\"}");
        HttpRequest request = HttpRequest.newBuilder()
            .POST(user)
            .uri(URI.create(url + authenticate))
            .header("Content-Type", "application/json")
            .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return "Something went wrong";
        } else {
            System.out.println(response.body());
            JsonNode body = mapper.readTree(response.body()).get("body");
            String jwtToken = body.get("token").asText();
            System.out.println(jwtToken);
            MainSceneController.jwt = jwtToken;
            MainSceneController.loggedIn = true;
            MainSceneController.user = getUser(username);
            return "Logged in";
        }
    }

}
