package dbg;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.Location;
import dbg.command.*;
import dbg.log.Logger;
import dbg.model.DebugModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ScriptableDebugger {

    private Class debugClass;
    private VirtualMachine vm;
    private Event currentEvent;
    private Intepreter intepreter;

    public VirtualMachine connectAndLaunchVM() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(debugClass.getName());
        VirtualMachine vm = launchingConnector.launch(arguments);
        return vm;
    }
    public void attachTo(Class debuggeeClass) {
        this.debugClass = debuggeeClass;
        try {
            vm = connectAndLaunchVM();
            enableClassPrepareRequest(vm);
            startDebugger();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            e.printStackTrace();
        } catch (VMStartException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (VMDisconnectedException e) {
            Logger.log("Virtual Machine is disconnected: " + e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableClassPrepareRequest(VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(debugClass.getName());
        classPrepareRequest.enable();
    }

    public void startDebugger() throws VMDisconnectedException, InterruptedException, AbsentInformationException {
        EventSet eventSet = null;
        intepreter = new Intepreter(this);
        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                System.out.println(event.toString());
                if (event instanceof VMDisconnectEvent ) {
                    Logger.log( "End of program " );
                    InputStreamReader reader =
                            new InputStreamReader (vm.process( ).getInputStream( ));
                    OutputStreamWriter writer = new OutputStreamWriter (System.out);
                    try {
                        reader.transferTo(writer);
                        writer.flush( );
                    }catch(IOException e ) {
                        Logger.log( " Target VM input stream reading error.");
                    }
                    Logger.saveLogFile();
                   return;
                }

                if(event instanceof ClassPrepareEvent) {
                    setBreakPoint(debugClass.getName(),6);
                    setBreakPoint(debugClass.getName(),10);
                }

                if (event instanceof StepEvent) {
                    vm.eventRequestManager().deleteEventRequest(
                            event.request()
                    );
                }
                if (event instanceof LocatableEvent){
                    currentEvent = event;
                    DebugModel model = new DebugModel(vm, (LocatableEvent) currentEvent);
                    processCommand(model);
                }

                if (event instanceof BreakpointEvent) {
                    BreakpointRequest bpReq = (BreakpointRequest) event.request();
                    if (bpReq.getProperty("break-once") != null) {
                        vm.eventRequestManager().deleteEventRequest(bpReq);
                        System.out.println("Breakpoint unique supprimé.");
                    }
                }
                vm.resume();
            }
        }
    }

    public BreakpointRequest setBreakPoint (String className , int lineNumber ) throws AbsentInformationException {
        for (ReferenceType targetClass : vm.allClasses())
            if(targetClass.name().equals(className)){
                Location location = targetClass.locationsOfLine(lineNumber).get(0);
                BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
            bpReq.enable();
            return bpReq;
        }
        return null;
    }

    public void processCommand(DebugModel model) {
        Scanner sc = new Scanner(System.in);

        boolean keepWaiting = true;

        while (keepWaiting) {
            System.out.print("Command > ");
            if (sc.hasNextLine()) {
                String input = sc.nextLine();
                String[] parts = input.split(" ");
                String commandKey = parts[0].toLowerCase();

                keepWaiting = intepreter.execute(model,commandKey,parts);

            }
        }
    }

    public VirtualMachine getVM() {
        return this.vm;
    }

    public Event getEvent() {
        return this.currentEvent;
    }
}
