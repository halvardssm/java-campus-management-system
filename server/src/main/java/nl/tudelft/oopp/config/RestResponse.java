package nl.tudelft.oopp.config;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RestResponse<T> implements Serializable {
    private T body;
    private String error;

    public RestResponse() {
    }

    /**
     * A generic REST response.
     *
     * @param body  The body
     * @param error The error, nullable
     */
    public RestResponse(T body, String error) {
        this.body = body;
        this.error = error;
    }

    public RestResponse(T body) {
        this.body = body;
        this.error = null;
    }

    /**
     * Creates a {@link ResponseEntity} with a body and error field.
     *
     * @param object     The object to place in the body field
     * @param error      The error to place in the error field
     * @param httpStatus The status code of the response
     * @return The finalized ResponseEntity
     */
    public static <T> ResponseEntity<RestResponse<T>> create(
        T object,
        @Nullable String error,
        @Nullable HttpStatus httpStatus
    ) {
        HttpStatus status = httpStatus == null ? HttpStatus.OK : httpStatus;
        return new ResponseEntity<>(
            new RestResponse<>(object, error),
            status
        );
    }

    public static <T> ResponseEntity<RestResponse<T>> create(T object) {
        return create(object, null, null);
    }

    public Object getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestResponse)) {
            return false;
        }
        RestResponse<?> that = (RestResponse<?>) o;
        return getBody().equals(that.getBody())
            && Objects.equals(getError(), that.getError());
    }
}
