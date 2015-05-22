package controller.bot;

public interface Command {
    enum Type{
        Lock, Start, PreCopyVar, CopyVar, PreCopyLine, CopyLine, TestIf, WriteIf, Move, Rotate, Stop
    }
    void execute();
    Type getType();
}
