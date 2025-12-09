package dbg.command;

import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

import java.util.List;

public class BreakBeforeMethodCallCommand implements Command {
    private final ScriptableDebugger debugger;

    public BreakBeforeMethodCallCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        if (args.length < 2) {
            return;
        }

        String methodName = args[1];

        for (ReferenceType targetClass : debugger.getVM().allClasses()) {

            List<Method> methods = targetClass.methodsByName(methodName);

            if (methods != null && !methods.isEmpty()) {
                Method method = methods.get(0);

                try {
                    Location location = method.location();

                    BreakpointRequest bpReq = debugger.getVM().eventRequestManager().createBreakpointRequest(location);
                    bpReq.enable();

                    Logger.log("Breakpoint installé au début de la méthode : "
                            + targetClass.name() + "." + methodName);
                    return;

                } catch (Exception e) {
                    Logger.log("Erreur lors de la création du breakpoint sur la méthode " + methodName);
                }
            }
        }

        Logger.log("Méthode '" + methodName + "' introuvable dans les classes utilisateur chargées.");
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}