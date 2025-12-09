package dbg.command;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.log.Logger;

import java.util.List;


public class SenderCommand implements Command{
    private final ScriptableDebugger debugger;

    public SenderCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();
        List<StackFrame> stack = null;
        try {
            stack = event.thread().frames();
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
        if (stack.size() < 2){
            Logger.log("Est le main");
            return null;
        }
        StackFrame frame = stack.get(1);
        return frame.thisObject();
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
