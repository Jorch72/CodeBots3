package controller.code.expressions;

import controller.bot.BotThread;

public class Constant extends Value{
    private final int value;
    public Constant(int value){
        this.value = value;
    }

    public int getValue(BotThread thread) {
        return value;
    }

    @Override
    public String toString() {
        return value+"";
    }
}
