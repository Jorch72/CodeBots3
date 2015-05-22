package controller.bot;

import controller.code.lines.Line;

public class BotPrototype {
    private final Line[] lines;
    private final String name;
    public BotPrototype(Line[] lines, String name){
        this.lines = lines;
        this.name = name;
    }

    public Line[] getLines() {
        return lines;
    }

    public String getName() {
        return name;
    }
}
