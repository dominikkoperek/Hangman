import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * this class is read attached file and choose one word with category
 * Class is used in GameService class for chose one word from list to hangman game
 */

public class WordRepository {
    private final static String FILE_NAME = "words.txt";
    private  final File file = new File(getClass().getClassLoader().getResource(FILE_NAME).getFile());
    private final Map<String, String> wordList = new HashMap<>();
    private final MessagePrinter messagePrinter;
    private final int generateRandomNumber = generateRandomNumber();
    /**
     * This construcor is for  dependency injection  of MessagePrinter class
     * @param messagePrinter dependency injection of MessagePrinter class
     */
    public WordRepository(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
        readAndPutInMap();
    }

    /**
     * This method count words from file
     * @return Total words lines in attached file
     */
    private int readAndCountLines() {
        int lineCount = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((bufferedReader.readLine()) != null) {
                lineCount++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            messagePrinter.printLine("File not found!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineCount;
    }

    /**
     * This method generate random number from 0 to Total lines of attached file
     * @return random number from 0 to total lines of file
     */
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(readAndCountLines());
    }

    /**
     * This method read file and put all words to Map. Keys are words and Values are words category.
     * File is .txt format
     * This method handles 2 exceptions (FileNotFoundExcepton and IOException).
     */

    private void readAndPutInMap() {
        String line;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                wordList.put(split[0].toLowerCase(), split[1]);
            }
        } catch (FileNotFoundException e) {
            messagePrinter.printLine("File not found!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method choose random word from generated generateRandomNumber method
     * and put it into map
     * @return map of 1 word and 1 value. The key is word and value is word category.
     */
    public Map<String, String> chooseWord() {
        Map<String, String> map = new HashMap<>();
        map.put(wordList.keySet().toArray()[generateRandomNumber].toString(),
                wordList.values().toArray()[generateRandomNumber].toString());
        return map;
    }

    /**
     * Method returns array of string with 6 elements representing 6 stages of user lives
     * @return Returns 6 elements Array of String each element is stage in game
     */
    public String [] hangmanVisualization(){
        return new String[]{
                """
      +---+
      |   |
      O   |
     /|\\  |
     / \\  |
          |
    =========
    """,
                """
      +---+
      |   |
      O   |
     /|\\  |
     /    |
          |
    =========
    """,
                """
      +---+
      |   |
      O   |
     /|\\  |
          |
          |
    =========
    """,
                """
      +---+
      |   |
      O   |
     /|   |
          |
          |
    =========
    """,
                """
      +---+
      |   |
      O   |
      |   |
          |
          |
    =========
    """,
                """
      +---+
      |   |
      O   |
          |
          |
          |
    =========
    """,
        };
    }
}
