package dbg.command;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

public class MethodCommand implements Command{
    private final ScriptableDebugger debugger;

    public MethodCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
            Location location = ((LocatableEvent) debugger.getEvent()).location();
            Logger.log("Method : " +location.method().name());
            model.setCurrentMethod(location.method().name());
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
