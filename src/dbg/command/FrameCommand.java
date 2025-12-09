package dbg.command;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

public class FrameCommand implements Command{
    private final ScriptableDebugger debugger;

    public FrameCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();
        try {
            StackFrame frame = event.thread().frame(0);
            Logger.log("Current frame: "
                    + frame.location().declaringType().name() + "."
                    + frame.location().method().name() + ":"
                    + frame.location().lineNumber());
            model.setCurrentFrame(frame);
        } catch (IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
