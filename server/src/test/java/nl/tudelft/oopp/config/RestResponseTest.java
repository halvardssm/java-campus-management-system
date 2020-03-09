package nl.tudelft.oopp.config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestResponseTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

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
}