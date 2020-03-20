package nl.tudelft.oopp.group39.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import nl.tudelft.oopp.group39.controllers.MainSceneController;

public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    private static String url = "http://localhost:8080/";
    private static String user = "user/";
    private static String building = "building/";
    private static String room = "room/";
    private static String authenticate = "authenticate/";
    private static String facility = "facility/";

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Retrieves users from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getUsers() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + "user")).build();
        return httpRequest(request);
    }

    /**
     * Retrieves user from the server based on username.
     *
     * @param username username of the user that needs to be retrieved
     * @return the body of a get request to the server.
     */
    public static String getUser(String username) {
        HttpRequest request =
            HttpRequest.newBuilder().GET().uri(URI.create(url + user + username)).build();
        return httpRequest(request);
    }

    /**
     * Retrieves bookings from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getBookings() {
        HttpRequest request = HttpRequest.newBuilder()
            .GET().uri(URI.create(url + "booking")).build();
        return httpRequest(request);
    }

    /**
     * Retrieves buildings from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getBuildings() {
        HttpRequest request = HttpRequest.newBuilder()
            .GET().uri(URI.create(url + "building")).build();
        return httpRequest(request);
    }

    /**
     * Retrieves rooms from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getRooms() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + "room")).build();
        return httpRequest(request);
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
            .uri(URI.create(url + room + buildingId))
            .build();
        return httpRequest(request);
    }

    /**
     * Retrieves facilities from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getFacilities() {
        HttpRequest request = HttpRequest.newBuilder().GET()
            .uri(URI.create(url + "facility")).build();
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
     * Adds a booking on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String addBooking(
        String date,
        String startTime,
        String endTime,
        String user,
        String room
    ) {
        HttpRequest.BodyPublisher newBooking = HttpRequest.BodyPublishers
            .ofString("{\"date\": \"" + date + "\", \"startTime\":\"" + startTime
                + "\", \"endTime\":\"" + endTime + "\", \"user\":\"" + user
                + "\", \"room\":\"" + room + "\"}");
        HttpRequest request = HttpRequest.newBuilder().POST(newBooking)
            .uri(URI.create(url + "booking/"))
            .header("Content-Type", "application/json").build();
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
     * Updates bookings on the server.
     *
     * @return the body of a put request to the server.
     */
    public static String updateBooking(
        String date,
        String startTime,
        String endTime,
        String user,
        String room,
        String id
    ) {
        HttpRequest.BodyPublisher newBooking = HttpRequest.BodyPublishers
            .ofString("{\"date\": \"" + date + "\", \"startTime\":\"" + startTime
                + "\", \"endTime\":\"" + endTime + "\", \"user\":\"" + user
                + "\", \"room\":\"" + room + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newBooking)
            .uri(URI.create(url + "booking/" + id))
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
     * @return the body of a put request to the server.
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
     * Retrieves all rooms from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getAllRooms() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + room)).build();
        return httpRequest(request);
    }

    /**
     * Removes a booking from the server.
     */
    public static void removeBooking(String id) {
        HttpRequest request = HttpRequest.newBuilder().DELETE()
            .uri(URI.create(url + "booking/" + id)).build();
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
    public static String addUser(String netID, String email, String password, String role) {
        HttpRequest.BodyPublisher newUser = HttpRequest.BodyPublishers
            .ofString("{\"username\": \"" + netID
                + "\", \"email\":\"" + email
                + "\", \"password\":\"" + password
                + "\", \"roles\":\"" + List.of(role) + "\"}");
        HttpRequest request = HttpRequest.newBuilder()
            .POST(newUser)
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
            MainSceneController.username = username;
            MainSceneController.isAdmin = isAdmin(username);
            return "Logged in";
        }
    }

    /**
     * Checks if user is an admin.
     *
     * @return boolean: true if user is admin, false otherwise
     */
    public static boolean isAdmin(String username) throws JsonProcessingException {
        String user = getUser(username);
        JsonNode userjson = mapper.readTree(user).get("body");
        String role = userjson.get("role").asText();
        return role.equals("ADMIN");
    }
}
