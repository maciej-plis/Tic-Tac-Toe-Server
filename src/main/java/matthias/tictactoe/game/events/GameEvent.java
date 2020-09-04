package matthias.tictactoe.game.events;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GameEvent {
    private GameEventType type;
    private Map<String, Object> payload;

    public static GameEventBuilder builder() {
        return new GameEventBuilder();
    }

    public static final class GameEventBuilder {
        private GameEventType type;
        private Map<String, Object> payload;

        private GameEventBuilder() {
            payload = new HashMap<>();
        }

        public GameEventBuilder eventType(GameEventType eventType) {
            this.type = eventType;
            return this;
        }

        public GameEventBuilder addToPayload(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }

        public GameEvent build() {
            GameEvent gameEvent = new GameEvent();
            gameEvent.type = this.type;
            gameEvent.payload = this.payload;
            return gameEvent;
        }
    }
}
