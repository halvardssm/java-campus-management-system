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
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;

public class ServerCommunication {

    public static String user = "user/";
    public static String building = "building/";
    public static String room = "room/";
    public static String authenticate = "authenticate/";
    public static String facility = "facility/";
    public static String booking = "booking/";
    public static String reservation = "reservation/";
    public static String food = "food/";
    public static String bike = "bike/";
    public static String event = "event/";
    private static HttpClient client = HttpClient.newBuilder().build();
    private static String url = "http://localhost:8080/";
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
     * Retrieves building filtered on id.
     *
     * @param id of wanted building
     * @return the body of a get request to the server.
     */
    public static String getBuilding(long id) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET().uri(URI.create(url + building + id)).build();
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
            .uri(URI.create(url + "room?building=" + buildingId))
            .build();
        return httpRequest(request);
    }

    /**
     * Retrieves the rooms with given filters.
     *
     * @param filters String representation of all the selected filters
     * @return the body of a get request to the server.
     */
    public static String getRooms(String filters) {
        System.out.println(url + "room?" + filters);
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url + "room?" + filters))
            .build();
        return httpRequest(request);
    }

    /**
     * Retrieves the room from the server based on the room id.
     *
     * @param roomId id of the room
     * @return the body of a get request to the server.
     */
    public static Room getRoom(long roomId) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url + room + roomId))
            .build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode roomJson = mapper.readTree(httpRequest(request)).get("body");
        String roomAsString = mapper.writeValueAsString(roomJson);
        return mapper.readValue(roomAsString, Room.class);
    }

    /**
     * Retrieves filtered list of buildings from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getFilteredBuildings(String filters) {
        String urlString = url + "building?" + filters;
        System.out.println(urlString);
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
        // return httpRequest(request);

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
            return "Booking created";
        }
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
        String id,
        String bookings
    ) {
        HttpRequest.BodyPublisher newBuilding = HttpRequest.BodyPublishers
            .ofString("{\"buildingId\": \"" + buildingId + "\", \"capacity\":\""
                + roomCapacity + "\", \"description\":\"" + roomDescription
                + "\", \"bookings\":\"" + bookings + "\"}");
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
     * Updates the user on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String updateUser(
        String username,
        String email,
        String password,
        String bookings
    ) {
        HttpRequest.BodyPublisher newUser = HttpRequest.BodyPublishers
            .ofString("{\"username\": \"" + username + "\", \"email\":\"" + email
                + "\", \"password\":\"" + password + "\", \"bookings\":\"" + bookings + "\"}");
        HttpRequest request = HttpRequest.newBuilder().PUT(newUser)
            .uri(URI.create(url + "user/" + username))
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
     * Retrieves all bookings from the server.
     *
     * @return the body of a get request to the server.
     */
    public static String getAllBookings() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + booking)).build();
        return httpRequest(request);
    }


    /**
     * Retrieves all bookings from the server.
     *
     * @param roomId the ID of the room
     * @param date   the chosen date where you want to get the bookings from
     * @return returns an HTTP request
     */
    public static String getBookings(int roomId, String date) {
        System.out.println(roomId);
        System.out.println(url + "booking?room="
            + roomId + "&date=" + date);
        HttpRequest request = HttpRequest.newBuilder()
            .GET().uri(URI.create(url + "booking?room="
                + roomId + "&date=" + date)).build();
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
     * Retrieves bikes filtered on building id.
     *
     * @param buildingId the id of the selected building
     * @return the body of a get request to the server.
     */
    public static String getBikes(int buildingId) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url + "bike?building=" + buildingId))
            .build();
        return httpRequest(request);
    }

    /**
     * Retrieves food filtered on building id.
     *
     * @param buildingId the id of the selected building
     * @return the body of a post request to the server.
     */
    public static String getFood(int buildingId) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url + "food?building=" + buildingId))
            .build();
        return httpRequest(request);
    }

    /**
     * Creates a reservation.
     *
     * @param timeOfPickup   date and time of receiving the order
     * @param timeOfDelivery date and time of returning the bike
     * @param user           netid of user making the order
     * @param roomId         id of the room the food needs to be delivered to
     * @param reservable     list of reservables
     * @return @return the body of a post request to the server.
     */
    public static String orderFoodBike(
        String timeOfPickup,
        String timeOfDelivery,
        String user,
        Integer roomId,
        String reservable
    ) {
        String timeofDeliv;
        if (timeOfDelivery == null) {
            timeofDeliv = null;
        } else {
            timeofDeliv = "\"" + timeOfDelivery + "\"";
        }
        String body = "{\"timeOfPickup\":\"" + timeOfPickup
            + "\",\"timeOfDelivery\":" + timeofDeliv
            + ",\"room\":" + roomId
            + ",\"user\":\"" + user
            + "\",\"reservationAmounts\":" + reservable + "}";
        System.out.println(body);
        HttpRequest.BodyPublisher newOrder = HttpRequest.BodyPublishers.ofString(body);
        HttpRequest request =
            HttpRequest.newBuilder()
                .POST(newOrder)
                .uri(URI.create(url + reservation))
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
            return "Order is placed";
        }
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
