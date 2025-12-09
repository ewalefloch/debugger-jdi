package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

import java.util.List;
import java.util.Map;


public class StackCommand implements Command{
    private final ScriptableDebugger debugger;

    public StackCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();
        try {
            List<StackFrame> frames = event.thread().frames();
            for(StackFrame frame : frames){
                System.out.println(frame.location().declaringType().name()
                        + "." + frame.location().method().name()
                        + ":" + frame.location().lineNumber());
            }
            model.setStacks(frames);
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
