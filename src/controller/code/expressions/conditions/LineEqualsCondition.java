package controller.code.expressions.conditions;

import controller.bot.BotThread;
import controller.code.expressions.LineNumber;

public class LineEqualsCondition extends Conditional{
    private final LineNumber line1, line2;
    public LineEqualsCondition(LineNumber line1, LineNumber line2){
        this.line1 = line1;
        this.line2 = line2;
    }

    @Override
    public boolean isTrue(BotThread thread) {
        return line1.getLine(thread).toString().equals(line2.getLine(thread).toString());
    }

    @Override
    public String toString() {
        return line1+"="+line2;
    }
}
