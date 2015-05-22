package controller;

import controller.bot.CodeBot;
import controller.ui.StartGUI;
import javafx.util.Pair;

import java.util.*;

public class Scoreboard {
    public final static int NUM_GAMES = 10;

    private int gameCount;
    private Game currentGame;
    private Random random;

    public final HashMap<String, Integer> points;
    public Scoreboard(Random random){
        points = new HashMap<String, Integer>();
        this.random = random;
    }

    public void start(){
        currentGame = new Game(random, this);
        gameCount++;
        StartGUI gui = new StartGUI(currentGame);
        gui.run();
    }


    public void finished(){
        for (CodeBot bot: currentGame.getAllBots()){
            String winner = bot.getLoyalty();
            if (!points.containsKey(winner)){
                points.put(winner, 0);
            }
            points.put(winner, points.get(winner)+1);
        }
        if (gameCount < NUM_GAMES) {
            start();
        }
    }

    public List<Pair<String, Integer>> getTopScores(){
        ValueComparator bvc =  new ValueComparator(points);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        sorted_map.putAll(points);
        List<Pair<String, Integer>> keySet = new ArrayList<Pair<String, Integer>>();
        for (String key: sorted_map.descendingKeySet()){
            keySet.add(new Pair<String, Integer>(key, sorted_map.get(key)));
        }
        return keySet;
    }
    class ValueComparator implements Comparator<String> {

        HashMap<String, Integer> base;
        public ValueComparator(HashMap<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

    public int getGameCount() {
        return gameCount;
    }
}
