package matthias.tictactoe.web.authentication.utils;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntityBuilder {

    private ResponseEntityBuilder() {}

    public static Builder status(int httpStatus) {
        return new Builder(httpStatus);
    }

    public static final class Builder {
        private int httpStatus;
        private Map<String, Object> payload = new HashMap<>();

        private Builder(int httpStatus) {
            this.httpStatus = httpStatus;
        }

        public Builder addToPayload(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }

        public ResponseEntity build() {
            return ResponseEntity
                    .status(httpStatus)
                    .body(payload);
        }
    }

}
