package controller.code.lines;

import controller.bot.BotThread;

public abstract class Line {
    public abstract void perform(BotThread bot);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Line) {
            Line line = (Line) obj;
            return line.toString().equals(toString());
        }
        return false;
    }
}
