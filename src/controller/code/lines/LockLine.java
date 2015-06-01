package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.LineNumber;
import controller.code.expressions.OpponentLineNumber;
import controller.parser.BadExpressionException;

public class LockLine extends Line{
    private final LineNumber lineNumber;

    private final String description;

    public LockLine(LineNumber lineNumber){
        if (lineNumber instanceof OpponentLineNumber) {
            throw new BadExpressionException("You cannot lock an opponent's line");
        }
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
