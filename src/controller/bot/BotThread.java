package controller.bot;

import controller.Game;

public class BotThread {
    private final CodeBot parent;
    private final int varNum;
    public BotThread(CodeBot parent, int varNum){
        this.parent = parent;
        this.varNum = varNum;
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
        getLine(getVariable(varNum).read()).read().perform(this);
    }

    public void addCommand(Command command){
        getParent().getGame().addTask(command);
    }

    public void increment(){
        BotVar var = getVariable(varNum);
        var.write(var.read() +1 % Game.MAX_INT);
    }
}
