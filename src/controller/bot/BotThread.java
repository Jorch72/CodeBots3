package controller.bot;

import controller.Game;

import java.util.HashSet;
import java.util.Set;

public class BotThread {
    private final CodeBot parent;
    private final int varNum;
    private final Set<Integer> executedLines;
    public BotThread(CodeBot parent, int varNum){
        this.parent = parent;
        this.varNum = varNum;
        executedLines = new HashSet<Integer>();
    }

    public BotVar getVariable(int varNum){
        return parent.getVariable(varNum, this);
    }

    public BotLine getLine(int varLine){
        return parent.getLine(varLine, this);
    }

    public void movePointer(int position){
        getVariable(varNum).write(position);
        addToExecution();
    }

    public CodeBot getParent() {
        return parent;
    }

    public int getVarNum() {
        return varNum;
    }

    @Override
    public int hashCode() {
        return parent.hashCode() ^ varNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BotThread) {
            BotThread botThread = (BotThread) obj;
            if (botThread.parent.equals(parent) && botThread.varNum == varNum){
                return true;
            }
        }
        return false;
    }

    public void addToExecution(){
        int lineNumber = getVariable(varNum).read();
        if (executedLines.contains(lineNumber))
            return;
        getLine(lineNumber).read().perform(this);
        executedLines.add(lineNumber);
    }

    public void addCommand(Command command){
        getParent().getGame().addTask(command);
    }

    public void increment(){
        executedLines.clear();
        BotVar var = getVariable(varNum);
        var.write(var.read() +1 % Game.MAX_INT);
    }
}
