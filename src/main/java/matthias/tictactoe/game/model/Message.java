package matthias.tictactoe.game.model;

import lombok.Getter;

import java.util.GregorianCalendar;

@Getter
public class Message {
    private String sender;
    private String message;
    private long timestamp;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.timestamp = new GregorianCalendar().getTimeInMillis();
    }
}
