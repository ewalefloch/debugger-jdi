package dbg.command;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;


public class ReceiverCommand implements Command{
    private final ScriptableDebugger debugger;

    public ReceiverCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();
        StackFrame frame = null;
        try {
            frame = event.thread().frame(0);
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
        return frame.thisObject();
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
