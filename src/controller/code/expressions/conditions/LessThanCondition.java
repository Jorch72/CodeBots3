package controller.code.expressions.conditions;

import controller.bot.BotThread;
import controller.code.expressions.Value;

public class LessThanCondition extends Conditional{
    private final Value val1, val2;
    public LessThanCondition(Value val1, Value val2){
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public boolean isTrue(BotThread thread) {
        return val1.getValue(thread) < val2.getValue(thread);
    }

    @Override
    public String toString() {
        return val1+"<"+val2;
    }
}
