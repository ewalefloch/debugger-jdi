package dbg.command;

import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;
import dbg.model.DebugModel;

public class StepOverCommand implements Command{
    private final ScriptableDebugger debugger;

    public StepOverCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        LocatableEvent event = (LocatableEvent) debugger.getEvent();

        StepRequest stepRequest =
                debugger.getVM().eventRequestManager().
                        createStepRequest ( event.thread(),
                                StepRequest.STEP_LINE,
                                StepRequest.STEP_OVER) ;
        stepRequest.enable();
    }

    @Override
    public boolean keepWaiting() {
        return false;
    }
}
