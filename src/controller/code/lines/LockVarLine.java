package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.Variable;

public class LockVarLine extends Line{
    private final Variable variable;
    private final String description;
    public LockVarLine(Variable variable){
        this.variable = variable;
        this.description = "Lock "+variable;
    }

    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                variable.getVar(bot).lockVar(bot);
            }

            @Override
            public Type getType() {
                return Type.Lock;
            }
        });
    }

    @Override
    public String toString() {
        return description;
    }
}
