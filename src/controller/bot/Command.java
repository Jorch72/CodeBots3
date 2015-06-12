package controller.bot;

public interface Command {
    enum Type{
        Lock, Start, PreCopyVar, PreCopyLine, CopyLine, CopyVar, TestIf, WriteIf, Move, Rotate, Stop
    }
    void execute();
    Type getType();
}
