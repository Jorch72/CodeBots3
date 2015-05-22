package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.Variable;

public class StartLine extends Line{
    private final Variable variable;
    private final String description;
    public StartLine(Variable variable){
        this.variable = variable;
        this.description = "Start "+variable;
    }

    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                variable.getVar(bot).startThread();
            }

            @Override
            public Type getType() {
                return Type.Start;
            }
        });
    }

    @Override
    public String toString() {
        return description;
    }
}
