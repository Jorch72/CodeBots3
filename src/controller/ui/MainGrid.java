package controller.ui;

import controller.Game;
import controller.bot.CodeBot;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

public class MainGrid extends JPanel {
    public final static int squareWidth = 11;
    public final static int squareHeight = 9;
    private Map<String, Character> botSymbols;
    private Map<Point, Color> highlights;
    private final Game game;
    public MainGrid(Game game){
        this.game = game;
        Dimension size = new Dimension(game.getMap().getWidth()*squareWidth, game.getMap().getHeight()*squareHeight);
        this.setPreferredSize(size);
        highlights = new HashMap<Point, Color>();
        botSymbols = new HashMap<String, Character>();
        HashSet<Character> used = new HashSet<Character>();
        for (CodeBot b: game.getAllBots()){
            String name = b.getPrototype().getName();
            if (botSymbols.containsKey(name))
                continue;
            boolean finished = false;
            for (int i = 0; i < name.length(); i++){
                if (!used.contains(name.toUpperCase().charAt(i))){
                    char c = name.toUpperCase().charAt(i);
                    botSymbols.put(name, c);
                    used.add(c);
                    finished = true;
                    break;
                } else if (!used.contains(name.toLowerCase().charAt(i))){
                    char c = name.toLowerCase().charAt(i);
                    botSymbols.put(name, c);
                    used.add(c);
                    finished = true;
                    break;
                }
            }
            if (finished)
                continue;
            for (int i = 33; i <= 687; i++){
                if (i == 127) {
                    i += 3;
                }else if (i == 141){
                    i+= 4;
                } else if (i==149 || i == 160){
                    i+=1;
                } else if (i == 157){
                    i+=2;
                }
                if (!used.contains((char) i)){
                    botSymbols.put(name, (char) i);
                    used.add((char) i);
                    finished = true;
                    break;
                }
            }

            if (finished)
                continue;
            throw new NoSuchElementException("Can't find character");
        }
    }

    public void addHighlight(int x, int y, Color color){
        highlights.put(new Point(x, y), color);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        for (int i = 0; i < game.getMap().getWidth(); i++) {
            for (int j = 0; j< game.getMap().getHeight(); j++){
                Point position = new Point(i, j);
                if (!game.getMap().botAt(position))
                    continue;
                CodeBot b = game.getMap().getBot(position);
                if (highlights.containsKey(position)){
                    g.setColor(highlights.remove(position));
                } else {
                    g.setColor(Color.black);
                }
                g.drawString(botSymbols.get(b.getPrototype().getName())+"", i*squareWidth, j*squareHeight+squareHeight);
            }
        }
    }
}
