package backend.academy.enums;

import java.security.SecureRandom;
import java.util.List;

public enum Difficulty {
    EASY("Легкий", 10),
    MEDIUM("Средний", 8),
    HARD("Тяжелый", 6);

    private final String russianName;
    private final int attempts;

    Difficulty(String russianName, int attempts) {
        this.russianName = russianName;
        this.attempts = attempts;
    }

    public String getRussianName() {
        return russianName;
    }

    public int getAttempts() {
        return attempts;
    }

    public static Difficulty getRandomDifficulty() {
        SecureRandom secureRandom = new SecureRandom();
        List<Difficulty> difficulties = List.of(Difficulty.values());
        int randomNumber = secureRandom.nextInt(difficulties.size());

        return difficulties.get(randomNumber);
    }
}
