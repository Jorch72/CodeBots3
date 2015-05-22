package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.Value;
import controller.code.expressions.conditions.Conditional;

public class IfLine extends Line{
    private final Conditional conditional;
    private final Value trueLine, falseLine;
    public final String description;
    public IfLine(Conditional conditional, Value trueLine, Value falseLine){
        this.conditional = conditional;
        this.trueLine = trueLine;
        this.falseLine = falseLine;
        description = "If "+conditional+" #"+trueLine+" #"+falseLine;
    }

    @Override
    public void perform(final BotThread bot) {
        final TrueTest test = new TrueTest();
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                test.isTrue = conditional.isTrue(bot);
            }

            @Override
            public Type getType() {
                return Type.TestIf;
            }
        });
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                if (test.isTrue){
                    bot.movePointer(trueLine.getValue(bot));
                } else {
                    bot.movePointer(falseLine.getValue(bot));
                }
            }

            @Override
            public Type getType() {
                return Type.WriteIf;
            }
        });
    }

    private class TrueTest{
        boolean isTrue;
    }

    @Override
    public String toString() {
        return description;
    }
}
