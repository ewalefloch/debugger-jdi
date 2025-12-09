package dbg.command;

import dbg.ScriptableDebugger;
import dbg.log.Logger;

public class ContinueCommand implements Command {
    private final ScriptableDebugger debugger;
    public ContinueCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        Logger.log("continue execution");
        return null;
    }

    @Override
    public boolean keepWaiting() {
        return false;
    }
}
