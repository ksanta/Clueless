package lol.karl.clueless;

import java.util.*;
import java.util.regex.Pattern;

public class Clue {
    // Each clue is made up of digits, where each digit represents some letter
    int[] digits;

    // The pattern is the digits with letters substituted in and dots for unknowns
    Pattern pattern;

    // Based on a dictionary, the words that match the pattern
    List<String> matchingWords = new ArrayList<>();

    public Clue(int[] digits) {
        this.digits = digits;
    }

    public boolean hasSureAnswer() {
        return matchingWords.size() == 1;
    }

    public void updatePatternAndMatchingWords(String[] solvedLetters, Set<String> dictionaryWords) {
        updatePattern(solvedLetters);
        updateMatchingWords(solvedLetters, dictionaryWords);
    }

    void updatePattern(String[] solvedLetters) {
        StringBuilder sb = new StringBuilder(digits.length);
        for (int num : digits) {
            String letter = solvedLetters[num];
            if (letter == null) {
                letter = ".";
            }
            sb.append(letter);
        }
        this.pattern = Pattern.compile(sb.toString());
    }

    private void updateMatchingWords(String[] solvedLetters, Set<String> dictionaryWords) {
        if (matchingWords.isEmpty()) {
            // Build up the matching words from the dictionary
            for (String dictionaryWord : dictionaryWords) {
                if (matches(dictionaryWord, solvedLetters)) {
                    matchingWords.add(dictionaryWord);
                }
            }
        } else {
            // Shrink down the words that are already matched
            matchingWords.removeIf(prematchedWord -> !matches(prematchedWord, solvedLetters));
        }
    }

    /**
     * Checks:
     * <ul>
     *  <li>The given word matches the regex pattern</li>
     *  <li>The solved letters cannot contain duplicate letters</li>
     *  <li>Different letters from the clue cannot refer to the same solved letter</li>
     * </ul>
     */
    boolean matches(String word, String[] solvedLetters) {
        // The given word must match the regex pattern
        if (!pattern.matcher(word).matches()) {
            return false;
        }

        String[] solvedCopy = Arrays.copyOf(solvedLetters, solvedLetters.length);
        for (int i = 0; i < pattern.pattern().length(); i++) {
            if (pattern.pattern().charAt(i) == '.') {
                // Cannot contain duplicate letters
                String charInWord = String.valueOf(word.charAt(i));
                if (Arrays.asList(solvedLetters).contains(charInWord)) {
                    return false;
                }
                // Must not change a letter that has already been set
                String charFromSolution = solvedCopy[digits[i]];
                if (charFromSolution != null && !charFromSolution.equals(charInWord)) {
                    return false;
                }
                solvedCopy[digits[i]] = charInWord;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clue{" +
                "digits=" + Arrays.toString(digits) +
                ", pattern=" + pattern +
                ", numMatchingWords=" + matchingWords.size() +
                '}';
    }
}
