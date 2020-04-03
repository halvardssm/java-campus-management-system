package nl.tudelft.oopp.group39.server.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;
import nl.tudelft.oopp.group39.user.model.User;

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
    public static String url;
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
     * Retrieves building filtered on id. The difference between the other getBuilding method
     * is that this method returns it as a building object.
     *
     * @param id of wanted building
     * @return the building.
     */
    public static Building getTheBuilding(long id) throws JsonProcessingException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url + building + id)).build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode roomJson = mapper.readTree(httpRequest(request)).get("body");
        String roomAsString = mapper.writeValueAsString(roomJson);
        return mapper.readValue(roomAsString, Building.class);
    }

    /**
     * Retrieves rooms from the server based on building id.
     *
     * @param input filter parameters
     * @return the body of a get request to the server.
     */
    public static String getRooms(String input) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url + "room?" + input))
            .build();
        return httpRequest(request);
    }

    /**
     * Retrieves the room from the server based on the room id.
     *
     * @param roomId id of the room
     * @return the body of a get request to the server.
     */
    public static Room getRoom(Long roomId) throws JsonProcessingException {
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
     * Adds a room on the server.
     *
     * @return the body of a post request to the server.
     */
    public static String addEvent(Event eventObj) throws JsonProcessingException {
        String eventJson = mapper.writeValueAsString(eventObj);
        HttpRequest.BodyPublisher newEvent = HttpRequest.BodyPublishers
            .ofString(eventJson);
        HttpRequest request = HttpRequest.newBuilder().POST(newEvent)
            .uri(URI.create(url + event))
            .header("Content-Type", "application/json").build();
        return httpRequest(request);
    }

    public static Event[] getEvent(String filters) throws JsonProcessingException {
        String urlString = url + "event?" + filters;
        System.out.println(urlString);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayNode eventJson = (ArrayNode) mapper.readTree(httpRequest(request)).get("body");
        String eventAsString = mapper.writeValueAsString(eventJson);
        return mapper.readValue(eventAsString, Event[].class);
    }

    public static String getEvents(String filters) {
        String urlString = url + "event?" + filters;
        System.out.println(urlString);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
        return httpRequest(request);
    }

    public static String updateEvent(Event eventObj, Long id) throws JsonProcessingException {
        String eventJson = mapper.writeValueAsString(eventObj);
        HttpRequest.BodyPublisher newEvent = HttpRequest.BodyPublishers
            .ofString(eventJson);
        HttpRequest request = HttpRequest.newBuilder().PUT(newEvent)
            .uri(URI.create(url + event + id))
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
            .uri(URI.create(url + booking + id))
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
            .uri(URI.create(url + room + id))
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
            .uri(URI.create(url + building + id))
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
     * Removes a building based on the id.
     *
     * @param id the id of the building
     */
    public static void removeBuilding(String id) {
        HttpRequest request = HttpRequest.newBuilder().DELETE()
            .uri(URI.create(url + "building/" + id)).build();
        httpRequest(request);
    }

    /**
     * Removes a room based on the id.
     *
     * @param id the id of the room in String.
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
     * @param filters String of filters
     * @return returns an HTTP request
     */
    public static String getBookings(String filters) {
        System.out.println(url + "booking?" + filters);
        HttpRequest request = HttpRequest.newBuilder()
            .GET().uri(URI.create(url + "booking?" + filters)).build();
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
    public static String getBikes(Long buildingId) {
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
    public static String getFood(Long buildingId) {
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
     * Retrieves reservations based on given filters.
     *
     * @param filters String of filters
     * @return @return the body of a get request to the server.
     */
    public static String getReservation(String filters) {
        System.out.println(url + "reservation?" + filters);
        HttpRequest request = HttpRequest.newBuilder()
            .GET().uri(URI.create(url + "reservation?" + filters)).build();
        return httpRequest(request);
    }

    /**
     * Sends an http request and receives string based on the request.
     *
     * @param req the request to be send
     * @return the body of a get request to the server, response code if not successful
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
            AbstractSceneController.jwt = jwtToken;
            AbstractSceneController.loggedIn = true;
            AbstractSceneController.user = getUser(username);
            return "Logged in";
        }
    }

}
