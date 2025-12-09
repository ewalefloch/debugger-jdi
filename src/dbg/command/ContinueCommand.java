package dbg.command;

import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

public class ContinueCommand implements Command {
    private final ScriptableDebugger debugger;
    public ContinueCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        Logger.log("continue execution");
        return;
    }

    @Override
    public boolean keepWaiting() {
        return false;
    }
}
