package controller.code.expressions.conditions;

import controller.bot.AccessException;
import controller.bot.BotThread;
import controller.code.expressions.LineNumber;

public class LockedLineCondition extends Conditional{
    private final LineNumber line;
    public LockedLineCondition(LineNumber line){
        this.line = line;
    }

    @Override
    public boolean isTrue(BotThread thread) {
        try {
            line.getLine(thread);
        } catch (AccessException exception){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "!"+line;
    }
}
