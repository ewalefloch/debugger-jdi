package dbg.command;

import com.sun.jdi.*;
import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

import java.util.List;
import java.util.Map;


public class TemporariesCommand implements Command{
    private final ScriptableDebugger debugger;

    public TemporariesCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        StackFrame frame = model.getCurrentFrame();
        if (frame == null) {
            Logger.log("Pas de frame");
            return;
        }
        try {
            List<LocalVariable> variables = frame.visibleVariables();
            Map<LocalVariable, Value> values = frame.getValues(variables);
            System.out.println("Temporaries:");
            for (Map.Entry<LocalVariable, Value> entry : values.entrySet()) {
                Logger.log(entry.getKey().name() + " -> " + entry.getValue());
            }
            model.setTemporaries(values);
        } catch (AbsentInformationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}
