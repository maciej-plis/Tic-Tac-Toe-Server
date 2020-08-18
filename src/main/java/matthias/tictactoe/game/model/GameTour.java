package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;

public class GameTour {
    private Symbol tour;

    public GameTour(Symbol tour) {
        this.tour = tour;
    }

    public Symbol getTour() {
        return tour;
    }

    public void setTour(Symbol tour) {
        this.tour = tour;
        GameEventPublisher.publishTurnChangedEvent(tour);
    }
}
