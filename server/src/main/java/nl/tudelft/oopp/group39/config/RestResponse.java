package nl.tudelft.oopp.group39.config;

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

    /**
     * A generic REST response.
     */
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

    /**
     * A generic REST response.
     *
     * @param body the body of the REST response
     */
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

    /**
     * Creates a {@link ResponseEntity} with a body and error field.
     *
     * @param object The object to place in the body field
     * @return The finalized ResponseEntity
     */
    public static <T> ResponseEntity<RestResponse<T>> create(T object) {
        return create(object, null, null);
    }

    /**
     * Creates a {@link ResponseEntity} with a body and error field.
     *
     * @param error The error to place in the error field
     * @return The finalized ResponseEntity
     */
    public static <T> ResponseEntity<RestResponse<T>> error(Exception error) {
        String message = (error.getMessage() != null && !error.getMessage().isEmpty())
                         ? error.getMessage()
                         : error.getClass().getName();

        return create(null, message, null);
    }

    /**
     * Gets the body of the REST response.
     *
     * @return the body of the rest respone
     */
    public Object getBody() {
        return body;
    }

    /**
     * Changes the body of the REST response.
     *
     * @param body the new body of the REST response
     */
    public void setBody(T body) {
        this.body = body;
    }

    /**
     * Gets the error message of the REST response.
     *
     * @return the error message of the REST response
     */
    public String getError() {
        return error;
    }

    /**
     * Changes the error of the REST response.
     *
     * @param error the new error of the REST response
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Checks whether two RestResponses are equal.
     *
     * @param o the other object
     * @return true if the two RestResponses are equal, false otherwise
     */
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
