package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

import java.util.List;
import java.util.Map;

public class ArgumentsCommand implements Command{
    private final ScriptableDebugger debugger;

    public ArgumentsCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
            Location location = ((LocatableEvent) debugger.getEvent()).location();
        try {
            StackFrame frame = ((LocatableEvent) debugger.getEvent()).thread().frame(0);
            List<LocalVariable> argsLocal = location.method().arguments();
            Map<LocalVariable, Value> values = frame.getValues(argsLocal);

            Logger.log("Args : ");
            for (Map.Entry<LocalVariable, Value> entry : values.entrySet()) {
                Logger.log(entry.getKey().name() + " -> " + entry.getValue());
            }

            model.setCurrentArgs(argsLocal);

        } catch (AbsentInformationException | IncompatibleThreadStateException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
