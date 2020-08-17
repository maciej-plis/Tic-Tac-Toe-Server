package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;

public class GameTour {
    private  final GameEventPublisher publisher;
    private Symbol tour;

    public GameTour(Symbol tour, GameEventPublisher publisher) {
        this.tour = tour;
        this.publisher = publisher;
    }

    public Symbol getTour() {
        return tour;
    }

    public void setTour(Symbol tour) {
        this.tour = tour;
        publisher.publishTurnChangedEvent(tour);
    }
}
