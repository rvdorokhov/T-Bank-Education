package backend.academy.classes.dictionaries;

import lombok.Getter;

@Getter
public class WordWithClue {
    public String word;
    public String clue;

    public WordWithClue(String word, String clue) {
        this.word = word;
        this.clue = clue;
    }
}
