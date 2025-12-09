package dbg.command;

public interface Command {
    Object execute(String[] args);
    boolean keepWaiting();
}
