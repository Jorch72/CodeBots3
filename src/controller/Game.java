package controller;

import controller.bot.*;
import controller.parser.FileParser;

import java.util.*;

public class Game {
    public final static int MAX_INT = 24;
    public final static int NUM_VARS = 4;
    public final static int MAX_TURNS = 5000;
    public final static int BOT_COPIES = 50;

    private int turnCounter = 0;

    private final HashMap<Command.Type, List<Command>> tasks;
    private final List<CodeBot> allBots;
    private final PriorityQueue<Command.Type> taskQueue;

    private final Map map;

    private final Scoreboard scoreboard;

    public boolean registered;




    public Game(Random random, Scoreboard scoreboard){
        taskQueue = new PriorityQueue<Command.Type>();

        tasks = new HashMap<Command.Type, List<Command>>();
        for (Command.Type type: Command.Type.values()){
            tasks.put(type, new ArrayList<Command>());
        }

        List<BotPrototype> prototypes = FileParser.readAllBots();
        allBots = new ArrayList<CodeBot>();
        for (BotPrototype prototype: prototypes){
            for (int i = 0; i < BOT_COPIES; i++){
                allBots.add(new CodeBot(prototype, this, random));
            }
        }

        map = new Map(this, random);
        this.scoreboard = scoreboard;
        registered = false;
    }

    public void step(){
        if (isFinished()){
            if (!registered) {
                scoreboard.finished();
            } else {
                registered = true;
            }
        }
        if (taskQueue.isEmpty()){
            for (CodeBot codeBot:allBots){
                for (BotThread thread: codeBot.getThreads()){
                    try {
                        thread.increment();
                    } catch (AccessException e){}
                }
            }
        } else {
            while (!taskQueue.isEmpty()) {
                Command.Type task = taskQueue.poll();
                List<Command> nextCommands = new ArrayList<Command>(tasks.get(task));
                for (Command command : nextCommands) {
                    try {
                        command.execute();
                    } catch (AccessException e) {
                    }
                }
                for (CodeBot bot: allBots){
                    bot.resolve(task);
                }
                if (task.equals(Command.Type.Move)){
                    getMap().resolveMovement();
                }
            }
        }
        turnCounter++;
    }

    public boolean isFinished(){
        return turnCounter == MAX_TURNS;
    }

    public void addTask(Command command){
        Command.Type type = command.getType();
        taskQueue.add(type);
        tasks.get(type).add(command);
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public List<CodeBot> getAllBots() {
        return allBots;
    }

    public Map getMap(){
        return map;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

}
