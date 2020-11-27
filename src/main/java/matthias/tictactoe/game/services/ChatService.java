package matthias.tictactoe.game.services;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.model.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ChatService {
    private final Consumer<GameEvent> eventCallback;

    private List<Message> messages;

    public ChatService(Consumer<GameEvent> eventCallback) {
        this.eventCallback = eventCallback;

        this.messages = new ArrayList<>();
    }

    public void newMessage(Message message) {
        messages.add(message);
        eventCallback.accept(GameEventFactory.createNewMessageEvent(message));
    }

    public Collection<Message> getMessages() {
        return this.messages;
    }

    public void clear() {
        this.messages.clear();
        eventCallback.accept(GameEventFactory.createClearChatEvent());
    }
}
