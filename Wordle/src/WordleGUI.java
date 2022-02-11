import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.*;

import java.awt.event.*;

import java.util.Arrays;

public class WordleGUI extends Frame {
    JFrame frame = new JFrame("Mini Game: Ja-Wordle-Va");
    int attempt = 1;
    Wordle wordle = new Wordle();

    JTextField[][] guesses = new JTextField[6][];

    ArrayList<String> gameRecord = new ArrayList<>();

    public WordleGUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
        }

        GridLayout layout = new GridLayout(7, 1, 0, 20);
        frame.setLayout(layout);

        Panel title = TitlePanel();
        frame.add(title);

        for (int i = 0; i < 6; i++) {
            JTextField[] guess = InputPanel();
            guesses[i] = guess;
        }

        frame.setVisible(true);
    }

    public Panel TitlePanel() {
        Panel title = new Panel();
        title.setSize(800, 800);
        title.setLayout(new GridLayout(2, 1));

        JLabel label1 = new JLabel("Ja - Wordle - Va");
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setFont(new Font("Courier", Font.PLAIN, 20));

        title.add(label1);

        JLabel label2 = new JLabel("Hi there, this is MengZe, the developer of this mini game!");
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setFont(new Font("Courier", Font.PLAIN, 14));

        title.add(label2);
        title.setVisible(true);
        return title;
    }

    public JTextField[] InputPanel() {
        Panel input = new Panel();
        input.setLayout(new GridLayout(1, 3, 20, 20));

        JTextField[] entries = new JTextField[5];

        for (int i = 0; i < 5; i++) {
            int current = i;
            JTextField entry = new JTextField("");
            entry.setHorizontalAlignment(JTextField.CENTER);
            entry.setFont(new Font("Courier", Font.PLAIN, 16));
            entry.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (entries[current].isEditable()) {
                            ArrayList<String> characters = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                String character = entries[i].getText();
                                characters.add(character);
                            }
                            String word = String.join("", characters);

                            int[] result = gameplay(word);

                            if (result.length != 1) {
                                for (int i = 0; i < 5; i++) {
                                    entries[i].setEditable(false);
                                    if (result[i] == 0) {
                                        entries[i].setBackground(new Color(144, 238, 144));
                                    } else if (result[i] == 1) {
                                        entries[i].setBackground(new Color(241, 235, 156));
                                    } else {
                                        entries[i].setBackground(new Color(211, 211, 211));
                                    }
                                }
                                guesses[attempt - 1][0].requestFocus();
                            } else {
                                JOptionPane.showMessageDialog(frame,
                                        "Invalid Input",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } else if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
                        if (current - 1 >= 0 && entries[current].getText().isEmpty()) {
                            entries[current - 1].requestFocus();
                        }
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        if (current - 1 >= 0) {
                            entries[current - 1].requestFocus();
                        }
                    } else if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                        if (current + 1 < 5) {
                            int index = current + 1;
                            entries[index].requestFocus();
                        }
                    }
                }

            });
            entries[i] = entry;
            input.add(entry);
        }

        frame.add(input);

        return entries;

    }

    public int[] gameplay(String guess) {
        boolean valid = wordle.validate(guess);

        if (!valid) {
            System.out.println("\nInvald word - Please try again!\n");
            int[] error = { 0 };
            return error;
        }

        int[] result = wordle.varify(guess);

        gameRecord.add(Arrays.toString(result));

        boolean win = wordle.checkWin(result);

        if (win) {
            JOptionPane.showMessageDialog(frame,
                    "Congratulations! You have guessed the word in " + attempt
                            + " attempts!\nYou can view your record in the record.txt!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            wordle.record(win, attempt, gameRecord.toString());
            System.exit(0);
        } else {
            if (attempt == 6) {
                wordle.record(win, 0, gameRecord.toString());
                JOptionPane.showMessageDialog(frame,
                        "The word is \'" + wordle.word + "\'. "
                                + "\nUnfortunately, you have run off of attempts! Try again later!\nYou can view your record in the record.txt!",
                        "Failure",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        attempt++;

        return result;
    }

    public static void main(String args[]) {
        new WordleGUI();
    }
}