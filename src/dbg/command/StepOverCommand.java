package dbg.command;

import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;

public class StepOverCommand implements Command{
    private final ScriptableDebugger debugger;

    public StepOverCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();

        StepRequest stepRequest =
                debugger.getVM().eventRequestManager().
                        createStepRequest ( event.thread(),
                                StepRequest.STEP_LINE,
                                StepRequest.STEP_OVER) ;
        stepRequest.enable();
        return null;
    }

    @Override
    public boolean keepWaiting() {
        return false;
    }
}
