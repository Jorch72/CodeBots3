package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.LineNumber;

public class LockLine extends Line{
    private final LineNumber lineNumber;

    private final String description;

    public LockLine(LineNumber lineNumber){
        this.lineNumber = lineNumber;
        description = "Lock "+lineNumber;
    }

    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                lineNumber.getLine(bot).lockLine(bot);
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
