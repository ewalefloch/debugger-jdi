package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;
import dbg.model.DebugModel;

import java.util.List;
import java.util.Map;

public class ReceiverVariablesCommand implements Command {
    private final ScriptableDebugger debugger;

    public ReceiverVariablesCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
            ObjectReference thisObject = model.getCurrentReceiver();

            if (thisObject == null) {
                System.out.println("Pas de variables d'instance ou non init");
                return;
            }

            ReferenceType referenceType = thisObject.referenceType();

            List<Field> fields = referenceType.visibleFields();

            Map<Field, Value> values = thisObject.getValues(fields);

            System.out.println("Receiver Variables:");
            for (Map.Entry<Field, Value> entry : values.entrySet()) {
                System.out.println("  " + entry.getKey().name() + " -> " + entry.getValue());
            }
            model.setVariableInstance(values);
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}