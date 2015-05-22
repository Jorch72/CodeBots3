package controller.code.lines;

import controller.bot.BotThread;

public class FlagLine extends Line {

    private final String description;
    private final String botName;
    public FlagLine(String botName){
        description = "Flag "+botName;
        this.botName = botName;
    }

    @Override
    public void perform(BotThread bot) {}

    @Override
    public String toString() {
        return description;
    }

    public String getBotName() {
        return botName;
    }
}
