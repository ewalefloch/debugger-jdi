package dbg.command;

import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;
import dbg.log.Logger;

public class StepCommand implements Command{
    private final ScriptableDebugger debugger;

    public StepCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();

        StepRequest stepRequest =
                debugger.getVM().eventRequestManager().
                        createStepRequest ( event.thread(),
                                StepRequest.STEP_MIN,
                                StepRequest.STEP_INTO);
        stepRequest.enable();
        return null;
    }

    @Override
    public boolean keepWaiting() {
        return false;
    }
}
