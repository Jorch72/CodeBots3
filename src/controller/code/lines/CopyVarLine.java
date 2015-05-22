package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.Value;
import controller.code.expressions.Variable;

public class CopyVarLine extends Line{
    private final Variable to;
    private final Value from;
    private final String description;
    public CopyVarLine(Value from, Variable to){
        this.to = to;
        this.from = from;
        description = "Copy "+from+" "+to;
    }
    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                to.getVar(bot).willWrite();
            }

            @Override
            public Type getType() {
                return Type.PreCopyVar;
            }
        });
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                to.getVar(bot).write(from.getValue(bot));
            }

            @Override
            public Type getType() {
                return Type.CopyVar;
            }
        });
    }

    @Override
    public String toString() {
        return description;
    }
}
