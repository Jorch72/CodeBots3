package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.OpponentVariable;
import controller.code.expressions.Variable;
import controller.parser.BadExpressionException;

public class LockVarLine extends Line{
    private final Variable variable;
    private final String description;
    public LockVarLine(Variable variable){
        if (variable instanceof OpponentVariable) {
            throw new BadExpressionException("You cannot lock an opponent's line");
        }
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
