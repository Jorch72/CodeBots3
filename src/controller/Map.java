package controller;

import controller.bot.AccessException;
import controller.bot.CodeBot;
import controller.code.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Map {
    private final Game parent;
    private final int width, height;
    private final HashMap<Point, CodeBot> currentMap;
    private final HashMap<CodeBot, Point> botPositions;
    private final HashMap<CodeBot, Point> futurePositions;
    private final HashMap<CodeBot, Direction> botDirections;
    public Map(Game parent, Random random){
        this.parent = parent;
        List<CodeBot> allBots = new ArrayList<CodeBot>(parent.getAllBots());
        Collections.shuffle(allBots, random);

        currentMap = new HashMap<Point, CodeBot>();
        botPositions = new HashMap<CodeBot, Point>();
        futurePositions = new HashMap<CodeBot, Point>();
        botDirections = new HashMap<CodeBot, Direction>();

        int botsPerLine = (int)Math.round(Math.sqrt(allBots.size()));
        width = botsPerLine*4;
        height = allBots.size()/botsPerLine;

        for (int i = 0; i < allBots.size(); i++){
            int y = i / botsPerLine;
            int x = (i%botsPerLine)*4+(y%2)*2;
            Point p = new Point(x, y);
            CodeBot bot = allBots.get(i);
            Direction d = Direction.values()[random.nextInt(Direction.values().length)];
            currentMap.put(p, bot);
            botPositions.put(bot, p);
            futurePositions.put(bot, p);
            botDirections.put(bot, d);
        }
    }
    public void move(CodeBot bot, Direction direction){
        Point newPosition = new Point(futurePositions.get(bot));
        newPosition.translate(direction.x, direction.y);
        if (newPosition.getX() < 0){
            newPosition.translate(width, 0);
        } else if (newPosition.getX() >= width){
            newPosition.translate(-width, 0);
        }
        if (newPosition.getY() < 0){
            newPosition.translate(0, height);
        } else if (newPosition.getY() >= height){
            newPosition.translate(0, -height);
        }
        futurePositions.put(bot, newPosition);
    }
    public void rotate(CodeBot bot, int clockwise){
        Direction rotated = Direction.values()[(botDirections.get(bot).ordinal()+clockwise)%Direction.values().length];
        botDirections.put(bot, rotated);
    }
    public void resolveMovement(){
        HashMap<Point, List<CodeBot>> futureLocations = new HashMap<Point, List<CodeBot>>();
        for (CodeBot bot: futurePositions.keySet()){
            Point location = futurePositions.get(bot);
            if (!futureLocations.containsKey(location)){
                futureLocations.put(location, new ArrayList<CodeBot>());
            }
            futureLocations.get(location).add(bot);
        }
        boolean hasDuplicates = true;
        while(hasDuplicates) {
            hasDuplicates = false;
            HashMap<Point, List<CodeBot>> locations = new HashMap<Point, List<CodeBot>>(futureLocations);
            futureLocations.clear();
            for (Point p : locations.keySet()) {
                List<CodeBot> bots =  locations.get(p);
                if (bots.size() > 1){
                    for (CodeBot bot: bots){
                        Point oldPosition = botPositions.get(bot);
                        futurePositions.put(bot, oldPosition);
                        if (!futureLocations.containsKey(oldPosition)){
                            futureLocations.put(oldPosition, new ArrayList<CodeBot>());
                        }
                        futureLocations.get(oldPosition).add(bot);
                    }
                    hasDuplicates = true;
                } else {
                    if (futureLocations.containsKey(p)){
                        futureLocations.get(p).addAll(bots);
                    } else {
                        futureLocations.put(p, bots);
                    }
                }
            }
        }
        for (CodeBot bot: futurePositions.keySet()){
            botPositions.put(bot, futurePositions.get(bot));
        }
        currentMap.clear();
        for (CodeBot bot: botPositions.keySet()){
            currentMap.put(botPositions.get(bot), bot);
        }
    }

    public boolean botAt(Point p){
        return currentMap.containsKey(p);
    }

    public CodeBot getBot(Point p){
        return currentMap.get(p);
    }

    public CodeBot getOpponent(CodeBot bot){
        Point p =  new Point(botPositions.get(bot));
        Direction d = botDirections.get(bot);
        p.translate(d.x, d.y);
        if (!currentMap.containsKey(p)){
            throw new AccessException();
        }
        return currentMap.get(p);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getBotPosition(CodeBot bot) {
        return botPositions.get(bot);
    }
}
