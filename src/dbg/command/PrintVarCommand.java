package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

public class PrintVarCommand implements Command {
    private final ScriptableDebugger debugger;

    public PrintVarCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        if (args.length < 2) {
            Logger.log("Erreur : Nom de variable manquant. Usage : print-var <variableName>");
            return;
        }

        String varName = args[1];
        StackFrame frame = model.getCurrentFrame();

        LocalVariable localVariable = null;

        try {
            localVariable = frame.visibleVariableByName(varName);
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }

        if (localVariable == null) {
            Logger.log("Erreur : Variable '" + varName + "' introuvable dans la frame courante.");
            return;
        }

        Value value = frame.getValue(localVariable);

        Logger.log(varName + " -> " + value.toString());
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}