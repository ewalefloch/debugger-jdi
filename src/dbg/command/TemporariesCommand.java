package dbg.command;

import com.sun.jdi.*;
import dbg.ScriptableDebugger;
import dbg.log.Logger;

import java.util.List;
import java.util.Map;


public class TemporariesCommand implements Command{
    private final ScriptableDebugger debugger;

    public TemporariesCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
        StackFrame frame = (StackFrame) new FrameCommand(debugger).execute(args);
        try {
            List<LocalVariable> variables = frame.visibleVariables();
            Map<LocalVariable, Value> values = frame.getValues(variables);
            System.out.println("Temporaries:");
            for (Map.Entry<LocalVariable, Value> entry : values.entrySet()) {
                Logger.log(entry.getKey().name() + " -> " + entry.getValue());
            }
            return values;
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
