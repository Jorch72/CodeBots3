package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.Direction;
import controller.code.expressions.Value;

public class MoveLine extends Line{
    private final Value value;
    public String description;
    public MoveLine(Value value){
        this.value = value;
        this.description = "Move "+value;
    }

    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                bot.getParent().moveBot(Direction.values()[value.getValue(bot)%Direction.values().length]);
            }

            @Override
            public Type getType() {
                return Type.Move;
            }
        });
    }

    @Override
    public String toString() {
        return description;
    }
}
