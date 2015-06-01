package controller.bot;

import controller.Game;
import controller.code.Direction;
import controller.code.lines.FlagLine;
import controller.code.lines.Line;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CodeBot {

    public Game getGame(){
        return parent;
    }

    private final BotVar[] variables;
    private final BotLine[] lines;
    private final Map<Integer, BotThread> threadPool;
    private final Random random;
    private final Game parent;
    private final BotPrototype prototype;
    public CodeBot(BotPrototype prototype, Game parent, Random random){
        this.random = random;
        this.parent = parent;
        this.prototype = prototype;
        variables = new BotVar[Game.NUM_VARS];
        for (int i = 0; i < Game.NUM_VARS; i++){
            variables[i] = new BotVar(i, this);
        }

        this.lines = new BotLine[Game.MAX_INT];
        for (int i = 0; i < Game.MAX_INT; i++){
            this.lines[i] = new BotLine(prototype.getLines()[i], this);
        }
        this.threadPool = new HashMap<Integer, BotThread>();
        addThreadToPool(Game.NUM_VARS-1, new BotThread(this, Game.NUM_VARS-1));
    }

    public void addThreadToPool(int varNum, BotThread threadToAdd){
        threadPool.put(varNum, threadToAdd);
    }

    public boolean hasThreadInPool(int varNum){
        return threadPool.containsKey(varNum);
    }

    public  BotVar getVariable(int varNum){
        return variables[varNum];
    }

    public BotLine getLine(int lineNum){
        return lines[lineNum];
    }

    public Random getRandom(){
        return random;
    }

    public void resolve(Command.Type command){
        switch (command){
            case Lock:
                resolveLocks();
                break;
            case CopyVar:
                resolveVarCopies();
                break;
            case CopyLine:
                resolveLineCopies();
        }
    }

    public BotVar getVariable(int varNum, BotThread thread){
        BotVar variable = getVariable(varNum);
        if (variable.threadCanAccess(thread)){
            return variable;
        }
        throw new AccessException();
    }

    public BotLine getLine(int varLine, BotThread thread){
        BotLine line = getLine(varLine);
        if (line.threadCanAccess(thread)){
            return line;
        }
        throw new AccessException();
    }

    private void resolveVarCopies() {
        for (BotVar var : variables) {
            var.resolveCopy();
        }
    }
    private void resolveLineCopies() {
        for (BotLine line: lines){
            line.resolveCopy();
        }
    }


    private void resolveLocks(){
        for (BotVar var: variables){
            var.resolveLocks();
        }
        for (BotLine line: lines){
            line.resolveLocks();
        }
    }

    public void removeThreadFromPool(int varNum) {
        for (BotVar var: variables){
            var.unlock();
        }
        for (BotLine line: lines){
            line.unlock();
        }
        threadPool.remove(varNum);
    }

    public void moveBot(Direction direction){
        parent.getMap().move(this, direction);
    }

    public Collection<BotThread> getThreads(){
        return threadPool.values();
    }

    public CodeBot getOpponent(){
        return parent.getMap().getOpponent(this);
    }

    public void rotate(int clockWise){
        parent.getMap().rotate(this, clockWise);
    }

    public BotPrototype getPrototype() {
        return prototype;
    }

    public String getLoyalty(){
        HashMap<String, Integer> loyalties = new HashMap<String, Integer>();
        int max = 0;
        for (BotLine line:lines){
            Line l = line.read();
            if (l instanceof FlagLine){
                String loyalty = ((FlagLine)l).getBotName();
                if (!loyalties.containsKey(loyalty)){
                    loyalties.put(loyalty, 0);
                }
                int newLoyalty = loyalties.get(loyalty)+1;
                loyalties.put(loyalty, newLoyalty);
                max = Math.max(newLoyalty, max);
            }
        }
        String next = null;
        for (String loyalty: loyalties.keySet()){
            if (loyalties.get(loyalty) == max){
                if (next != null)
                    return "";
                next = loyalty;
            }
        }
        return next;
    }
}
