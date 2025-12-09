package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;
import dbg.log.Logger;

public class BreakOnceCommand implements Command {
    private final ScriptableDebugger debugger;

    /***
        installe un point d’arrêt à
        la ligne lineNumber du fichier fileName. Ce point d’arrêt se désinstalle après
        avoir été atteint.
    ***/
    public BreakOnceCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        if (args.length < 3) {
            return null;
        }

        String className = args[1];
        int lineNumber = Integer.parseInt(args[2]);

        BreakpointRequest bpReq = null;
        try {
            bpReq = debugger.setBreakPoint(className, lineNumber);
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }

        if (bpReq != null) {
            bpReq.putProperty("break-once", true);
            Logger.log("Breakpoint unique ajouté à " + className + ":" + lineNumber);
        } else {
            Logger.log("Erreur lors de la création du breakpoint.");
        }

        return bpReq;
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}