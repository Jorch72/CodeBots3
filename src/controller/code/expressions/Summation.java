package controller.code.expressions;

import controller.Game;
import controller.bot.BotThread;

public class Summation extends Value {
    private final Value[] values;
    public Summation(Value...values){
        this.values = values;
    }
    @Override
    public int getValue(BotThread thread) {
        int total = 0;
        for (Value value: values){
            total+=value.getValue(thread);
        }
        return total% Game.MAX_INT;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Value value: values){
            builder.append(value).append(" ");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(")");
        return builder.toString();
    }
}
