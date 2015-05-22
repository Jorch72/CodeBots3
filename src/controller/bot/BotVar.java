package controller.bot;

import controller.Game;

public class BotVar {
    private BotThread currentLock;
    private BotThread futureLock;
    private boolean multipleLockAttempts;
    private boolean futureUnlock;

    private int writeCount;

    private final int index;
    private final CodeBot parent;
    private int value;
    public BotVar(int index, CodeBot parent){
        this.index = index;
        this.parent = parent;

        currentLock = null;
        futureLock = null;
        multipleLockAttempts = false;
        futureUnlock = false;
        writeCount = 0;
        value = 0;
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

    public void lockVar(BotThread thread){
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

    public void startThread(){
        if (!parent.hasThreadInPool(index)){
            parent.addThreadToPool(index, new BotThread(parent, index));
        }
    }

    public boolean hasThread(){
        return parent.hasThreadInPool(index);
    }

    public void willWrite(){
        writeCount++;
    }
    
    public int read(){
        if (writeCount > 0){
            return parent.getRandom().nextInt(Game.MAX_INT);
        }
        return value;
    }

    public void write(int value){
        if (writeCount > 1){
            this.value = parent.getRandom().nextInt(Game.MAX_INT);
        }
        this.value = value%24;
    }

    public void resolveCopy(){
        writeCount = 0;
    }

    public void stopThread(){
        if (parent.hasThreadInPool(index)){
            parent.removeThreadFromPool(index);
        }
    }

    @Override
    public String toString() {
        return value+"";
    }
}
