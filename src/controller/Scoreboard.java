package controller;

import controller.bot.CodeBot;
import javafx.util.Pair;

import java.util.*;

public class Scoreboard {

    private static int gameCount = 0;

    private static final HashMap<String, Integer> points = new HashMap<String, Integer>();

    private Scoreboard(){}

    public static void gameStarted(){
        gameCount++;
    }

    public static void gameFinished(Game game){
        for (CodeBot bot: game.getAllBots()){
            String winner = bot.getLoyalty();
            if (!points.containsKey(winner)){
                points.put(winner, 0);
            }
            points.put(winner, points.get(winner)+1);
        }
    }

    public static List<Pair<String, Integer>> getTopScores(){
        ValueComparator bvc =  new ValueComparator(points);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        sorted_map.putAll(points);
        List<Pair<String, Integer>> keySet = new ArrayList<Pair<String, Integer>>();
        for (java.util.Map.Entry<String, Integer> entry: sorted_map.entrySet()){
            keySet.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        return keySet;
    }
    static class ValueComparator implements Comparator<String> {

        HashMap<String, Integer> base;
        public ValueComparator(HashMap<String, Integer> base) {
            this.base = base;
        }

        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static int getGameCount() {
        return gameCount;
    }
}
