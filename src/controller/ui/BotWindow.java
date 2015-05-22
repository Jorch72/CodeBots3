package controller.ui;

import controller.Game;
import controller.bot.BotThread;
import controller.bot.CodeBot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BotWindow extends JFrame {
    private final List<JTextArea> code;
    private final JTextArea title;
    private final JTextArea vars;
    public final CodeBot bot;
    private final Game game;
    public BotWindow(CodeBot b, Game game){
        this.bot = b;
        this.game = game;
        this.setLayout(new GridLayout(Game.MAX_INT+2,1));
        title = new JTextArea(b.getPrototype().getName());
        title.setFont(new Font(null, 0, 20));
        this.add(title);
        vars = new JTextArea("");
        this.add(vars);
        code = new ArrayList<JTextArea>(Game.MAX_INT);
        for (int i = 0; i < Game.MAX_INT; i++){
            code.add(new JTextArea(""));
            this.add(code.get(i));
        }
        setValues();
        this.pack();
    }
    public void setValues(){
        String varChars = "";
        for (int i = 0; i < Game.NUM_VARS; i++){
            varChars+=((char)(i+'A'))+":"+bot.getVariable(i)+" ";
        }
        vars.setText(varChars);
        Set<Integer> threadIndices = new HashSet<Integer>();
        for (BotThread thread: bot.getThreads()){
            threadIndices.add(thread.getParent().getVariable(thread.getVarNum()).read());
        }
        for (int i = 0; i < Game.MAX_INT; i++){
            Color background = Color.white;
            if (threadIndices.contains(i)){
                background = Color.cyan;
            } else if (!bot.getPrototype().getLines()[i].equals(bot.getLine(i).read())){
                background = Color.lightGray;
            }
            code.get(i).setBackground(background);
            code.get(i).setText(bot.getLine(i).toString());
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        setValues();
    }
}
