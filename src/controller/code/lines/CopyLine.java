package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.LineNumber;

public class CopyLine extends Line{
    private final LineNumber to, from;
    private final String description;
    public CopyLine(LineNumber from, LineNumber to){
        this.to = to;
        this.from = from;
        description = "Copy "+to+" "+from;
    }
    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                to.getLine(bot).willWrite();
            }

            @Override
            public Type getType() {
                return Type.PreCopyLine;
            }
        });
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                to.getLine(bot).write(from.getLine(bot).read());
            }

            @Override
            public Type getType() {
                return Type.CopyLine;
            }
        });
    }

    @Override
    public String toString() {
        return description;
    }
}
