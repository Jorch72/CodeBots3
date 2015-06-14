package controller.code.expressions;

import controller.bot.BotThread;
import controller.bot.BotVar;

public class Variable extends Value {
    protected final int index;
    public Variable(int index){
        this.index = index;
    }
    public Variable(Variable variable){
        this.index = variable.index;
    }

    @Override
    public int getValue(BotThread thread) {
        return getVar(thread).read(thread);
    }


    public BotVar getVar(BotThread thread){
        return thread.getVariable(index);
    }

    @Override
    public int hashCode() {
        return this.index;
    }

    @Override
    public String toString() {
        return ((char)('A'+index))+"";
    }
}
