package controller.code.expressions.conditions;

import controller.bot.BotThread;

public abstract class Conditional {
    public abstract boolean isTrue(BotThread thread);
}
