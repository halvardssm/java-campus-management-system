package nl.tudelft.oopp.demo.communication;

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

    //IMPORTANT FOR SUNDAY
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

    public static String getData(String func) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/building")).build();
        switch(func) {
            case "building":
                request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/building")).build();
                break;
            case "room":
                request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/room")).build();
                break;
            case "filterBuilding":
                String urlString = "http://localhost:8080/FilterBuildings?capacity=10&building=e&location=new";
                request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
                break;
        }
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

    public static String getBuilding() {
        return getData("building");
    }
    public static String getRoom() {
        return getData("room");
    }

    public static String getFilteredBuildings(String name, String location){//, LocalTime open, LocalTime closed) {
//        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/building")).build();
        String urlString = "http://localhost:8080/FilterBuildings?capacity=10&building="+name+"&location="+location;//+"&open="+open+"&closed="+closed;
        return GenData(urlString);
    }

    public static String addBuilding(String name, String location, String description) {
//        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/building")).build();
        String urlString = "http://localhost:8080/addBuilding?building="+name+"&location="+location+"&description="+description;
        return GenData(urlString);
    }

    public static String GenData(String urlString) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build();
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
