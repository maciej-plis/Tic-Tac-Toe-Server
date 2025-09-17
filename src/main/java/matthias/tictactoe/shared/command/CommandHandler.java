package matthias.tictactoe.shared.command;

import java.util.function.Consumer;
import java.util.function.Function;

public interface CommandHandler {

    <T> T handle(Command<T> cmd);

    default <R, C extends Command<R>> R handle(C command, Function<C, R> handler) {
        return handler.apply(command);
    }

    default <R extends Void, C extends Command<R>> R handle(C command, Consumer<C> handler) {
        handler.accept(command);
        return null;
    }
}
