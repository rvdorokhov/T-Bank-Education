package backend.academy;

import backend.academy.game.GameSession;
import backend.academy.generators.DFSGenerator;
import backend.academy.generators.Generator;
import backend.academy.generators.PrimsGenerator;
import backend.academy.solvers.DFSSolver;
import backend.academy.solvers.DijkstraSolver;
import backend.academy.solvers.Solver;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        List<Solver> solvers = List.of(
            new DFSSolver(), new DijkstraSolver()
        );
        List<Generator> generators = List.of(
            new DFSGenerator(), new PrimsGenerator()
        );

        GameSession game = new GameSession(System.out, System.in, generators, solvers);
        game.startGame();
    }
}
