package dbg.command;

import dbg.ScriptableDebugger;
import dbg.log.Logger;
import dbg.model.DebugModel;

public class BreakCommand implements Command {
    private final ScriptableDebugger debugger;

    public BreakCommand(ScriptableDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void execute(DebugModel model, String[] args) {
        if (args.length < 3) {
            Logger.log("Erreur : Arguments manquants. Usage : break <ClassName> <LineNumber>");
            return;
        }

        String className = args[1];
        String lineNumberStr = args[2];

        try {
            int lineNumber = Integer.parseInt(lineNumberStr);

            debugger.setBreakPoint(className, lineNumber);

            Logger.log("Breakpoint ajouté à " + className + ":" + lineNumber);
            return;

        } catch (NumberFormatException e) {
            Logger.log("Erreur : Le numéro de ligne doit être un entier.");
        } catch (Exception e) {
            Logger.log("Erreur lors de la création du breakpoint : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean keepWaiting() {
        return true;
    }
}