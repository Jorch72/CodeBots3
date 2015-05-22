package controller.code.lines;

import controller.bot.BotThread;
import controller.bot.Command;
import controller.code.expressions.Value;

public class RotateLine extends Line{
    private final Value value;
    private final String description;
    public RotateLine(Value value){
        this.value = value;
        this.description = "Rotate "+value;
    }

    @Override
    public void perform(final BotThread bot) {
        bot.addCommand(new Command() {
            @Override
            public void execute() {
                bot.getParent().rotate(value.getValue(bot));
            }

            @Override
            public Type getType() {
                return Type.Rotate;
            }
        });
    }

    @Override
    public String toString() {
        return description;
    }
}
