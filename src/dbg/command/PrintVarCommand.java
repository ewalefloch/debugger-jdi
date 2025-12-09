package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.log.Logger;

public class PrintVarCommand implements Command {
    private final ScriptableDebugger debugger;

    public PrintVarCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        if (args.length < 2) {
            Logger.log("Erreur : Nom de variable manquant. Usage : print-var <variableName>");
            return null;
        }

        String varName = args[1];
        StackFrame frame = (StackFrame) new FrameCommand(debugger).execute(args);

        LocalVariable localVariable = null;

        try {
            localVariable = frame.visibleVariableByName(varName);
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }

        if (localVariable == null) {
            Logger.log("Erreur : Variable '" + varName + "' introuvable dans la frame courante.");
            return null;
        }

        Value value = frame.getValue(localVariable);

        Logger.log(varName + " -> " + value.toString());
        return value;
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}