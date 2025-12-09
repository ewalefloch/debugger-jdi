package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;
import java.util.Map;

public class ReceiverVariablesCommand implements Command {
    private final ScriptableDebugger debugger;

    public ReceiverVariablesCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public Object execute(String[] args) {
            ObjectReference thisObject = (ObjectReference) new ReceiverCommand(debugger).execute(args);

            if (thisObject == null) {
                System.out.println("Pas de variables d'instance");
                return null;
            }

            ReferenceType referenceType = thisObject.referenceType();

            List<Field> fields = referenceType.visibleFields();

            Map<Field, Value> values = thisObject.getValues(fields);

            System.out.println("Receiver Variables:");
            for (Map.Entry<Field, Value> entry : values.entrySet()) {
                System.out.println("  " + entry.getKey().name() + " -> " + entry.getValue());
            }
            return values;
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}