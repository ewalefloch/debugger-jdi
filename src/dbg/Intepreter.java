package dbg;

import dbg.command.*;

import java.util.HashMap;
import java.util.Map;

public class Intepreter {
    private final ScriptableDebugger debugger;
    private final Map<String, Command> commandMap = new HashMap<>();

    public Intepreter(ScriptableDebugger debugger){
        this.debugger = debugger;
        initCommand();
    }

    public boolean execute(String key,String[] args){
        if (commandMap.containsKey(key)) {
            Command cmd = commandMap.get(key);
            cmd.execute(args);
            return cmd.keepWaiting();
        } else {
            System.out.println("Commande inconnue.");
            return true;
        }
    }

    private void initCommand() {
        commandMap.put("step-over",new StepOverCommand(debugger));
        commandMap.put("step", new StepCommand(debugger));
        commandMap.put("continue", new ContinueCommand(debugger));
        commandMap.put("frame", new FrameCommand(debugger));
        commandMap.put("temporaries", new TemporariesCommand(debugger));
        commandMap.put("stack", new StackCommand(debugger));
        commandMap.put("receiver", new ReceiverCommand(debugger));
        commandMap.put("sender", new SenderCommand(debugger));
        commandMap.put("receiver-variables", new ReceiverVariablesCommand(debugger));
        commandMap.put("method", new MethodCommand(debugger));
        commandMap.put("arguments", new ArgumentsCommand(debugger));
        commandMap.put("print-var", new PrintVarCommand(debugger));
        commandMap.put("break", new BreakCommand(debugger));
        commandMap.put("breakpoints", new BreakpointsCommand(debugger));
        commandMap.put("break-once", new BreakOnceCommand(debugger));
        commandMap.put("break-on-count", new BreakOnCountCommand(debugger));
        commandMap.put("break-before-method-call", new BreakBeforeMethodCallCommand(debugger));


    }
}
