package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;
import dbg.log.Logger;

public class BreakOnCountCommand implements Command {
    private final ScriptableDebugger debugger;
    /***
        installe un point d’arrêt à la ligne lineNumber du fichier fileName.
     Ce point d’arrêt ne s’active qu’après avoir été atteint un certain nombre de fois count.
     ***/
    public BreakOnCountCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        if (args.length < 4) {
            return null;
        }

        String className = args[1];
        int lineNumber = Integer.parseInt(args[2]);
        int count = Integer.parseInt(args[3]);

        BreakpointRequest bpReq = null;
        try {
            bpReq = debugger.setBreakPoint(className, lineNumber);
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }

        if (bpReq != null) {
            bpReq.disable();

            bpReq.addCountFilter(count);

            bpReq.enable();

            Logger.log("Breakpoint configuré sur " + className + ":" + lineNumber + " (activé au " + count + "ème passage).");
        }

        return bpReq;
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}