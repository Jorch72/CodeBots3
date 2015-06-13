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

        int botsPerLine = (int)Math.round(Math.sqrt(allBots.size())/2);
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
        Set<CodeBot> toResolve = new HashSet<CodeBot>();
        HashMap<Point, CodeBot> nextLocations = new HashMap<Point, CodeBot>();
        for (CodeBot bot: parent.getAllBots()){
            Point location = futurePositions.get(bot);
            if (nextLocations.containsKey(location)){
                toResolve.add(nextLocations.get(location));
                toResolve.add(bot);
            } else {
                nextLocations.put(location, bot);
            }
        }
        for (CodeBot bot: toResolve) {
            revertBot(bot, nextLocations);
        }
        currentMap.clear();
        for (Point p : nextLocations.keySet()) {
            CodeBot b = nextLocations.get(p);
            botPositions.put(b, p);
            currentMap.put(p, b);
            futurePositions.put(b, p);
        }
    }

    private void revertBot(CodeBot bot, HashMap<Point, CodeBot> nextLocations){
        nextLocations.remove(futurePositions.get(bot));
        Point reverted = botPositions.get(bot);
        if (nextLocations.containsKey(reverted)){
            CodeBot nextBot = nextLocations.get(reverted);
            if (nextBot != bot) {
                revertBot(nextBot, nextLocations);
            }
        }
        nextLocations.put(reverted, bot);
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
