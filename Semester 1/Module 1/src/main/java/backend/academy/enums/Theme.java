package backend.academy.enums;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public enum Theme {
    ANIMALS("Животные"),
    COUNTRIES("Страны"),
    CITIES("Города"),
    FLOWERS("Цветы"),
    FRUITS("Фрукты"),
    COLORS("Цвета"),
    SPORTS("Виды спорта"),
    VEHICLES("Транспорт"),
    INSTRUMENTS("Инструменты"),
    CLOTHES("Одежда");

    private final String russianName;

    Theme(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    public static Theme getRandomTheme() {
        SecureRandom secureRandom = new SecureRandom();
        List<Theme> themes = new ArrayList<>(List.of(Theme.values()));
        int randomNumber = secureRandom.nextInt(themes.size());

        return themes.get(randomNumber);
    }
}
