package controller.code.expressions;

import controller.bot.BotLine;
import controller.bot.BotThread;

public class LineNumber {
    protected final Value value;
    public LineNumber(Value value){
        this.value = value;
    }
    public LineNumber(LineNumber number){
        this.value = number.value;
    }

    public BotLine getLine(BotThread thread){
        return thread.getLine(value.getValue(thread));
    }

    @Override
    public String toString() {
        return "#"+value.toString();
    }
}
