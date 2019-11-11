package lol.karl.clueless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Clue {
    // The maximum number of alternate answers to consider
    private static final int MAX_MATCHING_ANSWERS = 5;

    // Each clue is made up of digits, where each digit represents some letter
    int[] digits;

    // The pattern is the digits with letters substituted in and dots for unknowns
    Pattern pattern;

    // Based on a dictionary, the words that match the pattern
    List<String> possibleAnswers = new ArrayList<>();

    // The total number of words that the pattern matches in the dictionary
    int numPossibleAnswers = Integer.MAX_VALUE;

    public Clue(int[] digits) {
        this.digits = digits;
    }

    public boolean hasSureAnswer() {
        return possibleAnswers.size() == 1;
    }

    public void updatePatternAndMatchingWords(String[] solvedLetters, Dictionary dictionary) {
        updatePattern(solvedLetters);
        updateMatchingWords(solvedLetters, dictionary);
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

    private void updateMatchingWords(String[] solvedLetters, Dictionary dictionary) {
        Set<String> wordsByLength = dictionary.getWordsByLength(pattern.pattern().length());

        // todo: early skip if there are no clues (optimisation)

        possibleAnswers.clear();

        int numMatchingAnswers = 0;
        List<String> matchingAnswers = new ArrayList<>();

        for (String word : wordsByLength) {
            if (pattern.matcher(word).matches() && isValid(word, solvedLetters)) {
                if (numMatchingAnswers <= MAX_MATCHING_ANSWERS) {
                    matchingAnswers.add(word);
                }
                numMatchingAnswers++;
            }
        }

        numPossibleAnswers = numMatchingAnswers;

        // Update possible answers only if there are a "small" number
        if (numMatchingAnswers <= MAX_MATCHING_ANSWERS) {
            possibleAnswers.addAll(matchingAnswers);
        }
    }

    /**
     * Checks that letters are not being reused. Eg: "ER." cannot match "ERE" or "ERR".
     * "TH..." (21, 8, 5, 19, 5) rejects THREE and accept THERE
     */
    public boolean isValid(String word, String[] solvedLetters) {
        String[] solvedCopy = Arrays.copyOf(solvedLetters, solvedLetters.length);
        for (int i = 0; i < pattern.pattern().length(); i++) {
            if (pattern.pattern().charAt(i) == '.') {
                // Characters in the unknown spots cannot be already used
                String charInWord = String.valueOf(word.charAt(i));
                if (Arrays.asList(solvedLetters).contains(charInWord)) {
                    return false;
                }
                // Characters in the unknown spots must adhere to numbering in the clue
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
                ", possibleAnswers=" + possibleAnswers +
                ", numPossibleAnswers=" + numPossibleAnswers +
                '}';
    }
}
