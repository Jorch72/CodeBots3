package controller.bot;

import controller.code.lines.Line;

public class BotLine {
    private BotThread currentLock;
    private BotThread futureLock;
    private boolean multipleLockAttempts;
    private boolean futureUnlock;

    private int writeCount;

    private Line line;
    private final CodeBot parent;
    public BotLine(Line line, CodeBot parent){
        this.line = line;
        this.parent = parent;

        currentLock = null;
        futureLock = null;
        multipleLockAttempts = false;
        futureUnlock = false;
        writeCount = 0;
    }

    public boolean isLocked(){
        return currentLock != null;
    }

    public boolean isLockedBy(BotThread thread){
        return currentLock == thread;
    }

    public boolean threadCanAccess(BotThread thread){
        return !isLocked() || isLockedBy(thread);
    }

    public void lockLine(BotThread thread){
        if (isLockedBy(thread)) {
            futureUnlock = true;
        } else {
            if (futureLock != null) {
                multipleLockAttempts = true;
            } else {
                futureLock = thread;
            }
        }
    }

    public void resolveLocks() {
        if (futureUnlock) {
            currentLock = null;
        } else {
            if (!multipleLockAttempts) {
                currentLock = futureLock;
            }
        }
        futureUnlock = false;
        futureLock = null;
        multipleLockAttempts = false;
    }

    public void willWrite(){
        writeCount++;
    }

    public Line read(){
        if (writeCount > 0){
            throw new AccessException();
        }
        return line;
    }

    public void write(Line line){
        if (writeCount == 1){
            this.line = line;
        }
    }

    public void resolveCopy(){
        writeCount = 0;
    }

    @Override
    public String toString() {
        return line.toString();
    }
}
