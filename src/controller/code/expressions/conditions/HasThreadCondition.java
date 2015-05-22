package controller.code.expressions.conditions;

import controller.bot.AccessException;
import controller.bot.BotThread;
import controller.code.expressions.Variable;

public class HasThreadCondition extends Conditional{
    private final Variable variable;
    public HasThreadCondition(Variable variable){
        this.variable = variable;
    }

    @Override
    public boolean isTrue(BotThread thread) {
        try {
            variable.getVar(thread).hasThread();
        } catch (AccessException exception){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "?"+variable;
    }
}
