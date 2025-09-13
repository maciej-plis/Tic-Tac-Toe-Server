package matthias.tictactoe.tictactoe_game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

class EventPublisher<T> {

    private final Map<UUID, Consumer<T>> subscriptions = new HashMap<>();

    public void publishEvent(T event) {
        subscriptions.values().forEach(consumer -> consumer.accept(event));
    }

    public UUID subscribe(Consumer<T> subscription) {
        final var subscriptionId = UUID.randomUUID();
        subscriptions.put(subscriptionId, subscription);
        return subscriptionId;
    }

    public void unsubscribe(UUID subscriptionId) {
        subscriptions.remove(subscriptionId);
    }
}
