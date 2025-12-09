package dbg.command;

import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

import java.util.List;

public class BreakpointsCommand implements Command {
    private final ScriptableDebugger debugger;

    public BreakpointsCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        List<BreakpointRequest> breakpoints = debugger.getVM().eventRequestManager().breakpointRequests();

        if (breakpoints.isEmpty()) {
            Logger.log("Aucun point d'arrêt configuré.");
            return;
        }

        Logger.log("--- Liste des Breakpoints actifs ---");

        for (BreakpointRequest bp : breakpoints) {
            if (bp.isEnabled()) {
                String locationStr = bp.location().declaringType().name()
                        + ":" + bp.location().lineNumber();

                Logger.log(" -> " + locationStr);
            }
        }
        model.setBreakpoints(breakpoints);
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}