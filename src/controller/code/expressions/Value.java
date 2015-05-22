package controller.code.expressions;

import controller.bot.BotThread;

public abstract class Value {
    public abstract int getValue(BotThread thread);
}
