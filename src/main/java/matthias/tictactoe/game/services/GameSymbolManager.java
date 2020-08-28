package matthias.tictactoe.game.services;

import matthias.tictactoe.game.model.PlayerSymbol;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Scope("prototype")
public class GameSymbolManager {
    private final List<PlayerSymbol> availableSymbols = new ArrayList<>(Arrays.asList(PlayerSymbol.values()));

    public Optional<PlayerSymbol> getFirstAvailableSymbol() {
        if(availableSymbols.isEmpty()) {
            return Optional.empty();
        }

        Optional<PlayerSymbol> toReturn = Optional.of(availableSymbols.get(0));
        availableSymbols.remove(availableSymbols.get(0));
        
        return toReturn;
    }

    public void returnSymbol(PlayerSymbol symbol) {
        availableSymbols.add(symbol);
    }
}
