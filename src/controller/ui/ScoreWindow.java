package controller.ui;

import controller.Scoreboard;
import javafx.util.Pair;

import javax.swing.*;
import java.util.List;

public class ScoreWindow extends JFrame {
    public ScoreWindow(){
        JTextArea textField = new JTextArea();
        StringBuilder text = new StringBuilder();
        this.add(new JTextField());
        List<Pair<String, Integer>> scores = Scoreboard.getTopScores();
        for (int i = 0; i < scores.size(); i++){
            Pair<String, Integer> score = scores.get(i);
            text.append(i+1).append(". ").append(score.getValue()).append("\t").append(score.getKey()).append("\r\n");
        }
        textField.setText(text.toString());
        this.add(textField);
        this.pack();
        this.setTitle("Scores");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
