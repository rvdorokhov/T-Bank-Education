package backend.academy;

import backend.academy.classes.game.GameSession;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) throws Exception {
        GameSession game = new GameSession(System.out, System.in);
        game.startGame();
    }
}
