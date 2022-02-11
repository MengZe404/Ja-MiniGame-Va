import java.util.Scanner; // Import the Scanner class
import java.util.Arrays;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wordle {
    int length;

    private char[] target;
    String word;

    // Inspired by:
    // https://www.geeksforgeeks.org/how-to-print-colored-text-in-java-console/

    // Declaring ANSI_RESET so that we can reset the color
    public static final String ANSI_RESET = "\u001B[0m";

    // Declaring the color
    // Custom declaration
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    // constructor
    public Wordle() {
        String word = getWord();
        this.word = word.toLowerCase();
        this.target = wordParse(word);
        this.length = target.length;
    }

    public char[] wordParse(String word) {
        word = word.toLowerCase();

        char[] result = word.toCharArray();

        return result;
    }

    public boolean validate(String word) {
        String[] wordlist = getList();

        if (word.length() != this.length || !Arrays.asList(wordlist).contains(word)) {
            return false;
        }

        return true;
    }

    public int[] varify(String guess) {

        int[] result = new int[this.length];

        char[] word = wordParse(guess);

        for (int i = 0; i < this.length; i++) {
            if (word[i] == target[i]) {
                System.out.print(ANSI_GREEN + word[i] + ANSI_RESET);
                result[i] = 0;
            } else {
                if (new String(target).indexOf(word[i]) != -1) {
                    System.out.print(ANSI_YELLOW + word[i] + ANSI_RESET);
                    result[i] = 1;
                } else {
                    System.out.print(word[i]);
                    result[i] = -1;
                }
            }
        }

        System.out.println("");

        return result;
    }

    public boolean checkWin(int[] result) {
        boolean status = true;

        for (int i : result) {
            if (i != 0) {
                status = false;
                break;
            }
        }

        return status;
    }

    public void record(boolean win, int attempt, String gameRecord) {
        try {
            File file = new File("./bin/record.txt");

            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }

            FileWriter fwriter = new FileWriter(file, true);
            BufferedWriter bwriter = new BufferedWriter(fwriter);

            if (win) {
                bwriter.write(this.word + ", " + attempt + ", " + gameRecord);
                bwriter.newLine();
            } else {
                bwriter.write(this.word + ", 0" + ", " + gameRecord);
                bwriter.newLine();
            }
            bwriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String[] getList() {
        List<String> listOfStrings = new ArrayList<String>();

        try {
            File file = new File("./bin/word.txt");

            Scanner freader = new Scanner(file);

            while (freader.hasNextLine()) {
                String word = freader.nextLine().toLowerCase();
                listOfStrings.add(word);

            }

            freader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        String[] wordlist = listOfStrings.toArray(new String[0]);

        return wordlist;
    }

    public String getWord() {
        String[] wordlist = getList();
        Random random = new Random();

        int index = random.nextInt(wordlist.length);

        // System.out.println(wordlist[index]);

        return wordlist[index];
    }

    public void runGame() {
        // Initialize some variables

        int attempt = 1;

        Scanner scanner = new Scanner(System.in);

        String guess;

        ArrayList<String> gameRecord = new ArrayList<>();

        while (attempt <= 6) {

            guess = scanner.nextLine();

            boolean valid = validate(guess);

            if (!valid) {
                System.out.println("Invald word - Please try again!");
                continue;
            }

            int[] result = varify(guess);

            gameRecord.add(Arrays.toString(result));

            boolean win = checkWin(result);

            if (win) {
                System.out.println("Congratulations! You have guessed the word in " + attempt + " attempts!");
                record(win, attempt, gameRecord.toString());
                break;
            } else {
                if (attempt == 5) {
                    record(win, 0, gameRecord.toString());
                    System.out.println("The word is \'" + this.word + "\'!");
                    System.out.println("Unfortunately, you have run off of attempts! Try again later!");
                }
                attempt++;
            }
        }

        scanner.close();
    }
}
