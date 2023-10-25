import java.util.*;

/**
 * This Class is responsible for game logic.
 */
public class GameService {
    private final static String WORD_NOT_FOUND_MESS = "Word Not Found";
    private final Scanner scanner = new Scanner(System.in);
    private final MessagePrinter messagePrinter = new MessagePrinter();
    private final WordRepository wordRepository = new WordRepository(messagePrinter);
    private final Map<String, String> chosenWord = wordRepository.chooseWord();
    private final List<Character> chosenWordChar = mapChosenWordToCharList();
    private final List<Character> unguessedWord = mapChosenWordToUnderscore();
    private int lives = 6;

    /**
     * Create list of characters, where each character is an underscore ('_') of the same length as the word.
     * Create List of characters then put in list underscore char in the same amount what word length
     * In this way, the user sees the unguessed letters of the word.
     *
     * @return Returns List of characters containing ('_') of the same length as the chosenWord (class-level field 'chosenWord').
     */
    private List<Character> mapChosenWordToUnderscore() {
        List<Character> underScored = new ArrayList<>();
        String word = getWordToMap();
        for (int i = 0; i < word.length(); i++) {
            underScored.add('_');
        }
        return underScored;
    }

    /**
     * Create a list of characters and populates it with each letter from
     * the chosen word (class-level field 'chosenWord').
     *
     * @return Returns a list of characters containing the letters of the chosen word.
     */
    private List<Character> mapChosenWordToCharList() {
        List<Character> charList = new ArrayList<>();
        String wordToGuess = getWordToMap();
        for (int i = 0; i < wordToGuess.length(); i++) {
            charList.add(wordToGuess.charAt(i));
        }
        return charList;
    }

    private String getWordToMap() {
        return chosenWord
                .keySet()
                .stream()
                .findFirst()
                .orElse(WORD_NOT_FOUND_MESS);
    }

    /**
     * Main game logic method that starts by printing basic game information using the getStartInformation() method.
     * The method enters a while loop to check if the player has more than 1 lives (class-level field 'lives').
     * The user inputs a letter or word using the typeWord() method, which returns a String.
     * Multiple condition checks are performed, including checking if the input String is 1 character or more.
     * If the input is more than 1 character, a check is performed to see if it matches the chosen word.
     * If the user doesn't guess the word correctly, lives are reduced by -2 (or by -1 if the user has only 1 life remaining).
     * If the user guesses the word correctly, a loop replaces all underscore characters ('_') from the unguessed list with the correct letters.
     * The method prints information about the current state of the game.
     * An if statement checks  both arrays (unguessed and chosenWord) are the same, signaling the end of the game.
     * The game concludes with a win or lose message, depending on the player's remaining lives.
     */
    public void startGame() {
        getStartInformation();
        while (lives > 0) {
            String userLetterOrWord = typeWord();
            if (isUserGiveFullWord(userLetterOrWord)) {
                String wordToGuess = getWordToMap();
                if (wordToGuess.equals(userLetterOrWord)) {
                    replaceAllUnderscoreToLetters(userLetterOrWord);
                } else {
                    if (lives == 1) {
                        lives--;
                    } else {
                        lives -= 2;
                    }
                    messagePrinter.printLine(wordRepository.hangmanVisualization()[lives]);
                    messagePrinter.printLine("Wrong word! " + lives + " left");
                }

            } else if (checkIfWordContainsUserOneLetter(userLetterOrWord)) {
                replaceUnderscoreToGuessedLetter(userLetterOrWord);
                messagePrinter.printLine("Nice one! You guessed letter.");
            } else {
                lives--;
                messagePrinter.printLine(wordRepository.hangmanVisualization()[lives]);
                messagePrinter.printLine("Wrong letter! " + lives + " lives left");

            }
            printUnguessedWord();
            if (checkIfWin()) {
                messagePrinter.printLine("____YOU WIN____");
                messagePrinter.printLine(lives + "<-- Lives left");
            }
        }
        messagePrinter.printLine("____YOU LOSE____");
        messagePrinter.printLine("The word was: " + chosenWord.keySet());
    }

    /**
     * Replaces all occurrences of underscore characters in the 'unguessedWord' list (a class-level field) with the user's input letter.
     *
     * @param userLetterOrWord The letter that the user has entered as input (in this case single char).
     */
    private void replaceUnderscoreToGuessedLetter(String userLetterOrWord) {
        for (int i = 0; i < chosenWordChar.size(); i++) {
            if (chosenWordChar.get(i).equals(userLetterOrWord.charAt(0))) {
                unguessedWord.set(i, userLetterOrWord.charAt(0));
            }
        }
    }

    /**
     * Replaces all underscore character in the 'unguessedWord' list (a class-level field) with the correct letters of chosenWord (class-level field 'chosenWord')
     *
     * @param userLetterOrWord The word that the user has entered as input (in this case more than 1 char).
     */
    private void replaceAllUnderscoreToLetters(String userLetterOrWord) {
        for (int i = 0; i < unguessedWord.size(); i++) {
            unguessedWord.set(i, userLetterOrWord.charAt(i));
        }
    }

    private static boolean isUserGiveFullWord(String userLetterOrWord) {
        return userLetterOrWord.length() > 1;
    }

    private boolean checkIfWin() {
        return lives >= 1 && chosenWordChar.equals(unguessedWord);
    }

    private boolean checkIfWordContainsUserOneLetter(String userLetterOrWord) {
        return userLetterOrWord.length() == 1 && chosenWordChar.contains(userLetterOrWord.charAt(0));
    }

    private String typeWord() {
        return scanner.nextLine().toLowerCase();
    }

    private void printUnguessedWord() {
        for (Character character : unguessedWord) {
            messagePrinter.print(character.toString());
            messagePrinter.print(" ");
        }
        messagePrinter.printLine("");
    }

    private void getStartInformation() {
        String categoryName = getCategoryName();
        int chosenWordSize = chosenWordChar.size();
        messagePrinter.printLine("____________________________________________________________________________");
        messagePrinter.printLine("__HANGMAN__");
        messagePrinter.printLine(lives + " <-- Lives left ");
        messagePrinter.printLine("You can try to guess the whole word at once, " +
                "but if you make a mistake, you will lose 2 lives!");
        messagePrinter.printLine("_______________________________________________________________________");
        messagePrinter.printLine("The word has: [ " + chosenWordSize
                + " ] letters | category is: [ " + categoryName + " ]");
        printUnguessedWord();
    }


    private String getCategoryName() {
        return chosenWord
                .values()
                .stream()
                .findFirst()
                .orElse(WORD_NOT_FOUND_MESS);
    }
}
