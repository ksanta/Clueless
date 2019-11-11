package lol.karl.clueless;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Clueless {

    public static class Clue {
        int[] digits;
        List<String> possibleAnswers = new ArrayList<>();
        int numPossibleAnswers = Integer.MAX_VALUE;
        Pattern pattern;

        public Clue(int[] digits) {
            this.digits = digits;
        }

        public boolean hasSureAnswer() {
            return possibleAnswers.size() == 1;
        }

        public void updatePattern(String[] solvedLetters) {
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

        public void updateMatchingWords(String[] solvedLetters, Dictionary dictionary) {
            final int maxMatchingAnswers = 5;  // For performance, limit the number of matching answers to consider
            Set<String> wordsByLength = dictionary.getWordsByLength(pattern.pattern().length());

            // todo: early skip if there are no clues (optimisation)

            possibleAnswers.clear();

            int numMatchingAnswers = 0;
            List<String> matchingAnswers = new ArrayList<>();

            for (String word : wordsByLength) {
                if (pattern.matcher(word).matches() && isValid(word, solvedLetters)) {
                    if (numMatchingAnswers <= maxMatchingAnswers) {
                        matchingAnswers.add(word);
                    }
                    numMatchingAnswers++;
                }
            }

            numPossibleAnswers = numMatchingAnswers;

            // Update possible answers only if there are a "small" number
            if (numMatchingAnswers <= maxMatchingAnswers) {
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

    private Dictionary dictionary;

    public Clueless() throws IOException {
        this.dictionary = new Dictionary();
    }

    /**
     * @param clues         all the incoming clues in the puzzle
     * @param solvedLetters the starter clues, which will be populated
     * @param guessDepth    the number of guesses we are relying on at this point
     * @return the answer to the puzzle
     */
    public String solve(List<Clue> clues, String[] solvedLetters, Clue finalPuzzle, int guessDepth) {
        findSureAnswers(clues, solvedLetters);

        // Only try and solve the final answer if there are currently no guesses or if all the clues are successfully used
        if (guessDepth == 0 || clues.isEmpty()) {
            System.out.println("Attempting to solve final answer");
            finalPuzzle.updatePattern(solvedLetters);
            finalPuzzle.updateMatchingWords(solvedLetters, dictionary);
            if (finalPuzzle.hasSureAnswer()) {
                System.out.println("Final clue " + finalPuzzle.pattern.pattern() + " can only be " + finalPuzzle.possibleAnswers.get(0));
                return finalPuzzle.possibleAnswers.get(0);
            }
        }

        if (!clues.isEmpty()) {
            // Find clue with least alternatives and remove from list of clues
            clues.sort(Comparator.comparingInt(clue -> clue.numPossibleAnswers));
            Clue clueWithLeastAlternatives = clues.get(0);

            if (clueWithLeastAlternatives.numPossibleAnswers == 0) {
                System.out.println("Clue " + clueWithLeastAlternatives.pattern + " has no solution!");
                return null;
            }

            clues.remove(clueWithLeastAlternatives);

            // Attempt to solve using each possible alternative
            for (String possibleAnswer : clueWithLeastAlternatives.possibleAnswers) {
                System.out.println("Branching out, trying " + possibleAnswer + ", out of " + clueWithLeastAlternatives.possibleAnswers);
                String[] prospectiveSolvedLetters = createNewSolvedLettersWithAnswer(clueWithLeastAlternatives, possibleAnswer, solvedLetters);
                String finalAnswer = solve(clues, prospectiveSolvedLetters, finalPuzzle, guessDepth + 1);
                if (finalAnswer != null) {
                    return finalAnswer;
                }
            }

            System.out.println("Backtracking, re-adding " + clueWithLeastAlternatives.pattern);
            clues.add(clueWithLeastAlternatives);
        }

        return null;
    }

    /**
     * Keeps iterating to find sure answers until there are no more sure answers
     */
    private void findSureAnswers(List<Clue> clues, String[] solvedLetters) {
        boolean newLetterDiscovered = true;
        while (newLetterDiscovered) {
            // Update the state of the clues with the latest known info
            for (Clue clue : clues) {
                clue.updatePattern(solvedLetters);
                clue.updateMatchingWords(solvedLetters, dictionary);
            }

            System.out.println("Scanning " + clues.size() + " clues for sure answers");
            newLetterDiscovered = false;
            Iterator<Clue> clueIterator = clues.iterator();
            while (clueIterator.hasNext()) {
                Clue clue = clueIterator.next();

                if (clue.hasSureAnswer()) {
                    System.out.println("Clue " + clue.pattern.pattern() + " can only be " + clue.possibleAnswers.get(0));
                    updateSolvedLettersWithAnswer(clue, clue.possibleAnswers.get(0), solvedLetters);
                    newLetterDiscovered = true;
                    clueIterator.remove();
                }
            }
        }
        System.out.println("No more sure answers");
    }

    private String[] createNewSolvedLettersWithAnswer(Clue clue, String answer, String[] solvedLetters) {
        String[] newSolvedLetters = Arrays.copyOf(solvedLetters, solvedLetters.length);
        updateSolvedLettersWithAnswer(clue, answer, newSolvedLetters);
        return newSolvedLetters;
    }

    private void updateSolvedLettersWithAnswer(Clue clue, String answer, String[] solvedLetters) {
        for (int i = 0; i < clue.digits.length; i++) {
            int clueNumber = clue.digits[i];
            if (solvedLetters[clueNumber] == null) {
                char discoveredLetter = answer.charAt(i);
                System.out.println("  Solved " + clueNumber + " = " + discoveredLetter);
                solvedLetters[clueNumber] = String.valueOf(discoveredLetter);
            }
        }
    }
}
