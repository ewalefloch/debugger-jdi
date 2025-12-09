package dbg.model;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;

import java.util.List;
import java.util.Map;

public class DebugModel {
    private VirtualMachine vm;
    private LocatableEvent event;

    private StackFrame currentFrame;
    private ObjectReference currentReceiver;
    private Map<LocalVariable, Value> temporaries;
    private List<StackFrame> stacks;
    private ObjectReference sender;
    private Map<Field, Value> variableInstance;
    private String currentMethod;
    private List<LocalVariable> currentArgs;
    private List<BreakpointRequest> breakpoints;

    public DebugModel(VirtualMachine vm, LocatableEvent event) {
        this.vm = vm;
        this.event = event;
    }

    public VirtualMachine getVm() {
        return vm;
    }

    public LocatableEvent getEvent() {
        return event;
    }

    public StackFrame getCurrentFrame() {
        return currentFrame;
    }

    public ObjectReference getCurrentReceiver() {
        return currentReceiver;
    }

    public void setCurrentFrame(StackFrame currentFrame){
        this.currentFrame = currentFrame;
    }

    public void setCurrentReceiver(ObjectReference currentReceiver){
        this.currentReceiver = currentReceiver;

    }

    public Map<LocalVariable, Value> getTemporaries() {
        return temporaries;
    }

    public void setTemporaries(Map<LocalVariable, Value> temporaries) {
        this.temporaries = temporaries;
    }

    public List<StackFrame> getStacks() {
        return stacks;
    }

    public void setStacks(List<StackFrame> stacks) {
        this.stacks = stacks;
    }

    public ObjectReference getSender() {
        return sender;
    }

    public void setSender(ObjectReference sender) {
        this.sender = sender;
    }

    public Map<Field, Value> getVariableInstance() {
        return variableInstance;
    }

    public void setVariableInstance(Map<Field, Value> variableInstance) {
        this.variableInstance = variableInstance;
    }

    public String getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(String currentMethod) {
        this.currentMethod = currentMethod;
    }

    public List<LocalVariable> getCurrentArgs() {
        return currentArgs;
    }

    public void setCurrentArgs(List<LocalVariable> currentArgs) {
        this.currentArgs = currentArgs;
    }

    public List<BreakpointRequest> getBreakpoints() {
        return breakpoints;
    }

    public void setBreakpoints(List<BreakpointRequest> breakpoints) {
        this.breakpoints = breakpoints;
    }
}