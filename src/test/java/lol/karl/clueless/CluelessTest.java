package lol.karl.clueless;

import lol.karl.clueless.Clueless.Clue;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CluelessTest {

    @Test
    public void testClueIsValid() {
        String[] solvedLetters = new String[27];
        solvedLetters[9] = "E";
        solvedLetters[14] = "R";

        Clue clue = new Clue(new int[]{9, 14, 8});
        clue.updatePattern(solvedLetters);

        assertThat(clue.isValid("ERR", solvedLetters), is(false));
        assertThat(clue.isValid("ERA", solvedLetters), is(true));
    }

    @Test
    public void testClueIsvalidComplexCase() {
        String[] solvedLetters = new String[27];
        solvedLetters[9] = "T";
        solvedLetters[14] = "H";

        Clue clue = new Clue(new int[]{9, 14, 8, 1, 8});
        clue.updatePattern(solvedLetters);

        assertThat(clue.isValid("THERE", solvedLetters), is(true));
        assertThat(clue.isValid("THREE", solvedLetters), is(false));
    }

    @Test
    public void solveDecember2019() throws IOException {
        Clueless clueless = new Clueless();

        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue(new int[]{20, 21, 4, 9, 23, 25}));
        clues.add(new Clue(new int[]{25, 3, 3, 4, 24, 7}));
        clues.add(new Clue(new int[]{4, 2, 19, 1, 19, 22, 12}));
        clues.add(new Clue(new int[]{12, 16, 4, 19, 13, 13, 7}));
        clues.add(new Clue(new int[]{5, 19, 2, 19, 2, 19}));
        clues.add(new Clue(new int[]{20, 26, 22, 23, 13, 13}));
        clues.add(new Clue(new int[]{3, 26, 19, 23, 2}));
        clues.add(new Clue(new int[]{9, 19, 9, 20}));
        clues.add(new Clue(new int[]{19, 15, 12, 9}));
        clues.add(new Clue(new int[]{11, 25, 4, 22, 2, 12, 7}));
        clues.add(new Clue(new int[]{18, 25, 19, 2}));
        clues.add(new Clue(new int[]{1, 23, 14, 12}));
        clues.add(new Clue(new int[]{19, 5, 25, 8, 12}));

        String[] solvedLetters = new String[27];
        solvedLetters[9] = "D";
        solvedLetters[19] = "A";
        solvedLetters[22] = "R";

        Clue finalPuzzle = new Clue(new int[]{12, 2, 11, 25, 7, 20});

        String solution = clueless.solve(new ArrayList<>(clues), solvedLetters, finalPuzzle, 0);

        System.out.println("Final solution: " + solution);

        assertThat(solution, is("ENJOYS"));
    }

    @Test
    public void solveNovember2019() throws IOException {
        Clueless clueless = new Clueless();

        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue(new int[]{25, 13, 16, 10, 19, 24, 15, 7}));
        clues.add(new Clue(new int[]{1, 16, 23, 21, 8, 13}));
        clues.add(new Clue(new int[]{26, 18, 26, 15, 24, 26, 19, 24, 13}));
        clues.add(new Clue(new int[]{1, 4, 15, 13, 17}));

        String[] solvedLetters = new String[27];
        solvedLetters[1] = "S";
        solvedLetters[3] = "M";
        solvedLetters[13] = "E";

        Clue finalPuzzle = new Clue(new int[]{19, 24, 15, 21, 4});

        String solution = clueless.solve(new ArrayList<>(clues), solvedLetters, finalPuzzle, 0);

        printSolvedLetters(solvedLetters);

        System.out.println("Final solution: " + solution);

        assertThat(solution, is("BLINK"));
    }

    @Test
    public void solveSeptember2019() throws IOException {
        Clueless clueless = new Clueless();

        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue(new int[]{3, 11, 14, 18, 23, 3}));
        clues.add(new Clue(new int[]{3, 1, 9, 19, 22, 11, 9, 3}));
        clues.add(new Clue(new int[]{23, 8, 7, 7, 13, 13, 10}));
        clues.add(new Clue(new int[]{10, 13, 21, 11, 9, 14, 9}));
        clues.add(new Clue(new int[]{19, 9, 15, 12, 13}));
        clues.add(new Clue(new int[]{8, 22, 22, 18, 3, 9, 16}));
        clues.add(new Clue(new int[]{22, 13, 20, 9, 14, 3}));
        clues.add(new Clue(new int[]{7, 13, 5, 8, 7, 19, 5}));
        clues.add(new Clue(new int[]{8, 3, 11, 8, 15, 9, 16}));
        clues.add(new Clue(new int[]{15, 5, 3, 9, 7, 2}));
        clues.add(new Clue(new int[]{19, 14, 13, 19, 19, 9, 14}));
        clues.add(new Clue(new int[]{9, 17, 19, 14, 8}));
        clues.add(new Clue(new int[]{9, 14, 8}));
        clues.add(new Clue(new int[]{8, 15, 9, 14, 25, 22, 8}));
        clues.add(new Clue(new int[]{3, 24, 18, 9, 9, 26, 9}));
        clues.add(new Clue(new int[]{3, 12, 8, 14, 1, 7, 9, 16}));
        clues.add(new Clue(new int[]{22, 11, 8, 3, 9, 3}));

        String[] solvedLetters = new String[27];
        solvedLetters[13] = "O";
        solvedLetters[14] = "R";
        solvedLetters[19] = "T";

        Clue finalPuzzle = new Clue(new int[]{23, 7, 8, 26, 9, 3});

        String solution = clueless.solve(new ArrayList<>(clues), solvedLetters, finalPuzzle, 0);

        printSolvedLetters(solvedLetters);

        System.out.println("Final solution: " + solution);

        assertThat(solution, is("BLAZES"));
    }

    @Test
    public void solveOctober2014() throws IOException {
        Clueless clueless = new Clueless();

        List<Clue> clues = new ArrayList<>();
        // Across
        clues.add(new Clue(new int[]{26, 15, 19, 23, 4}));
        clues.add(new Clue(new int[]{24, 4, 4, 17, 20, 23, 4, 18, 6}));
        clues.add(new Clue(new int[]{16, 15, 15}));
        clues.add(new Clue(new int[]{24, 20, 18}));
        clues.add(new Clue(new int[]{18, 20, 3, 6, 3, 17, 23}));
        clues.add(new Clue(new int[]{4, 5, 3, 24}));
        clues.add(new Clue(new int[]{11, 10, 17, 3, 6, 13, 21, 4, 17, 24}));
        clues.add(new Clue(new int[]{24, 13, 4, 21, 4}));
        clues.add(new Clue(new int[]{13, 20, 18, 9, 4, 6, 24, 4, 19}));
        clues.add(new Clue(new int[]{20, 11, 11, 15, 3, 17, 24, 4, 19}));
        clues.add(new Clue(new int[]{3, 19, 4, 20, 6}));
        clues.add(new Clue(new int[]{11, 4, 20, 8, 4, 2, 10, 26, 26, 25}));
        clues.add(new Clue(new int[]{14, 10, 24, 4}));
        clues.add(new Clue(new int[]{3, 17, 6, 24, 20, 17, 24}));
        clues.add(new Clue(new int[]{25, 4, 6}));
        clues.add(new Clue(new int[]{24, 4, 20}));
        clues.add(new Clue(new int[]{19, 4, 6, 8, 4, 17, 19, 4, 19}));
        clues.add(new Clue(new int[]{6, 3, 19, 4, 6}));

        String[] solvedLetters = new String[27];
        solvedLetters[19] = "D";
        solvedLetters[25] = "Y";
        solvedLetters[4] = "E";  // I added this clue myself because lots of words end with "ED"

        Clue finalClue = new Clue(new int[]{8, 18, 3, 21, 6, 15, 17});

        String solution = clueless.solve(new ArrayList<>(clues), solvedLetters, finalClue, 0);

        printSolvedLetters(solvedLetters);

        System.out.println("Final solution: " + solution);

        assertThat(solution, is("CRIMSON"));
    }

    private void printSolvedLetters(String[] solvedLetters) {
        System.out.print("Known letters: ");
        for (int i = 1; i < solvedLetters.length; i++) {
            String letter = solvedLetters[i];
            if (letter == null) {
                letter = "-";
            }
            System.out.print(letter + ", ");
        }
        System.out.println();
    }
}