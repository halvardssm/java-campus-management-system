package nl.tudelft.oopp.group39.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class RestResponseTest {
    @Test
    void constructorBody() {
        RestResponse<?> response = new RestResponse<>(new ArrayList<>());
        Assertions.assertNotNull(response);
    }

    @Test
    void setAndGetBody() {
        List<String> list = new ArrayList<>(List.of("test"));
        RestResponse<List<String>> response = new RestResponse<>(new ArrayList<>());
        response.setBody(list);
        Assertions.assertEquals(list, response.getBody());
    }

    @Test
    void setAndGetError() {
        RestResponse<List<String>> response = new RestResponse<>(new ArrayList<>());
        response.setError("test");
        Assertions.assertEquals("test", response.getError());
    }

    @Test
    void testEqualsClone() {
        RestResponse<List<String>> response = new RestResponse<>(new ArrayList<>());
        Assertions.assertEquals(response, response);
    }

    @Test
    void testEqualsNull() {
        RestResponse<List<String>> response = new RestResponse<>(new ArrayList<>());
        RestResponse<List<String>> response2 = new RestResponse<>(new ArrayList<>());

        Assertions.assertEquals(response, response2);
    }

    @Test
    void testEqualsFalse() {
        RestResponse<List<String>> response = new RestResponse<>(new ArrayList<>());
        RestResponse<List<String>> response2 = new RestResponse<>(new LinkedList<>());
        Assertions.assertEquals(response, response2);
    }

    @Test
    void testCreate() {
        List<String> body = List.of("test");
        String error = "error";
        HttpStatus status = HttpStatus.ACCEPTED;
        ResponseEntity<RestResponse<Object>> response = RestResponse.create(body, error, status);

        Assertions.assertEquals(body, response.getBody().getBody());
        Assertions.assertEquals(error, response.getBody().getError());
        Assertions.assertEquals(status, response.getStatusCode());
    }
}