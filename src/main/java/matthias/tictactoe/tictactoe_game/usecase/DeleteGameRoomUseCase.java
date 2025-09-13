package matthias.tictactoe.tictactoe_game.usecase;

public class DeleteGameRoomUseCase extends ConsumerUseCase<String> {
    private final GameRoomRepository gameRoomRepository;

    public DeleteGameRoomUseCase(GameRoomRepository gameRoomRepository) {
        this.gameRoomRepository = gameRoomRepository;
    }

    @Override
    public void accept(String roomId) {
        gameRoomRepository.deleteById(roomId);
    }
}
