package lol.karl.clueless;

import java.io.IOException;
import java.util.*;

public class Clueless {

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
            finalPuzzle.updatePatternAndMatchingWords(solvedLetters, dictionary);
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
                clue.updatePatternAndMatchingWords(solvedLetters, dictionary);
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
