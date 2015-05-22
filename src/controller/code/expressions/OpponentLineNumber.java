package controller.code.expressions;

import controller.bot.BotLine;
import controller.bot.BotThread;

public class OpponentLineNumber extends LineNumber{
    public OpponentLineNumber(LineNumber number){
        super(number);
    }

    @Override
    public BotLine getLine(BotThread thread) {
        return thread.getParent().getOpponent().getLine(super.value.getValue(thread), thread);
    }

    @Override
    public String toString() {
        return "*"+super.toString();
    }
}
