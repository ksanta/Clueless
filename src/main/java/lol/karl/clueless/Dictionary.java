package lol.karl.clueless;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dictionary {

    private Set<String> words = new HashSet<>(370_000);
    private Map<Integer,Set<String>> wordsByLength = new HashMap<>();

    public Dictionary() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/lol/karl/clueless/english3.txt"));

        String word = reader.readLine();
        while (word != null) {
            final String uppercaseWord = word.toUpperCase();

            // words contains all words
            words.add(uppercaseWord);

            // wordsByLength contains all words grouped by length
            int wordLength = uppercaseWord.length();
            wordsByLength.computeIfAbsent(wordLength, k -> new HashSet<>());
            wordsByLength.get(wordLength).add(uppercaseWord);

            word = reader.readLine();
        }
    }

    public boolean containsWord(String word) {
        return words.contains(word);
    }

    public Set<String> getWordsByLength(int length) {
        return wordsByLength.get(length);
    }
}
