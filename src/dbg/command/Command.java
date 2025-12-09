package dbg.command;

import dbg.model.DebugModel;

public interface Command {
    void execute(DebugModel model, String[] args);
    boolean keepWaiting();
}
