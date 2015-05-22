package controller.code.expressions;

import controller.Game;
import controller.bot.BotThread;
import controller.bot.BotVar;

public class OpponentVariable extends Variable{
    public OpponentVariable(Variable variable){
        super(variable);
    }

    @Override
    public BotVar getVar(BotThread thread){
        return thread.getParent().getOpponent().getVariable(super.index, thread);
    }


    @Override
    public int hashCode() {
        return super.hashCode()+ Game.MAX_INT;
    }

    @Override
    public String toString() {
        return "*"+super.toString();
    }
}
