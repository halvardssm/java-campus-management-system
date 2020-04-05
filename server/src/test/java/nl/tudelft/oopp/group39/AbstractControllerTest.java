package nl.tudelft.oopp.group39;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.config.abstracts.IEntity;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    protected String jwt;

    protected String generateParams() {
        return null;
    }

    protected void listTest(String url, Map<String, Matcher<?>> map) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(url))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)));

        for (String key : map.keySet()) {
            Matcher<?> value = map.get(key);
            resultActions.andExpect(jsonPath("$.body[0]." + key, value));
        }
    }

    protected void listFilterTest(String url, Map<String, String> map) throws Exception {
        mockMvc.perform(get(url))
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)));
    }

    protected <E extends IEntity> void deleteTest(String url, E object) throws Exception {
        mockMvc.perform(delete(url + "/" + object.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist())
            .andExpect(jsonPath("$.error").doesNotExist())
            .andDo((result) -> {
                object.setId(null);
            });
    }

    protected <E extends IEntity> void createTest(
        String url,
        E object,
        Map<String, Matcher<?>> map
    ) throws Exception {
        String json = objectMapper.writeValueAsString(object);

        ResultActions resultActions = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated());

        for (String key : map.keySet()) {
            Matcher<?> value = map.get(key);
            resultActions.andExpect(jsonPath("$.body." + key, value));
        }

        resultActions.andDo((result) -> {
            String responseString = result.getResponse().getContentAsString();
            JsonNode productNode = new ObjectMapper().readTree(responseString);
            object.setId(productNode.get("body").get("id").longValue());
        });
    }

    protected <E extends IEntity> void readTest(String url, E object, Map<String, Matcher<?>> map)
        throws Exception {
        ResultActions resultActions = mockMvc.perform(get(url + "/" + object.getId()))
            .andExpect(jsonPath("$.body").isMap());

        for (String key : map.keySet()) {
            Matcher<?> value = map.get(key);
            resultActions.andExpect(jsonPath("$.body." + key, value));
        }
    }

    protected <E extends IEntity> void updateTest(String url, E object, Map<String, Matcher<?>> map)
        throws Exception {
        String json = objectMapper.writeValueAsString(object);

        ResultActions resultActions = mockMvc.perform(put(url + "/" + object.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap());

        for (String key : map.keySet()) {
            Matcher<?> value = map.get(key);
            resultActions.andExpect(jsonPath("$.body." + key, value));
        }
    }
}
