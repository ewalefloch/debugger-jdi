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
import com.sun.jdi.request.StepRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Scanner;

public class ScriptableDebugger {

    private Class debugClass;
    private VirtualMachine vm;

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
            System.out.println("Virtual Machine is disconnected: " + e.toString());
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

    public void startDebugger() throws VMDisconnectedException, InterruptedException {
        EventSet eventSet = null;
        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                System.out.println(event.toString());
                if (event instanceof VMDisconnectEvent ) {
                    System.out.println( "End of program " );
                    InputStreamReader reader =
                            new InputStreamReader (vm.process( ).getInputStream( ));
                    OutputStreamWriter writer = new OutputStreamWriter (System.out);
                    try {
                        reader.transferTo(writer);
                        writer.flush( );
                    }catch(IOException e ) {
                        System.out.println( " Target VM input stream reading error.");
                    }
                   return;
                }

                if(event instanceof ClassPrepareEvent) {
                    setBreakPoint(debugClass.getName(),6);
                }

                if(event instanceof BreakpointEvent) {
                    stepCommand((LocatableEvent) event);
                }

                if (event instanceof StepEvent) {
                    vm.eventRequestManager().deleteEventRequest(
                            event.request()
                    );
                    stepCommand((LocatableEvent) event);
                }

                vm.resume();
            }
        }

    }

    public void enableStepRequests(LocatableEvent event) {
        StepRequest stepRequest =
                vm. eventRequestManager().
                        createStepRequest ( event.thread(),
                StepRequest.STEP_MIN,
                StepRequest.STEP_OVER) ;
        stepRequest.enable();
    }

    public void setBreakPoint (String className , int lineNumber ) {
        for ( ReferenceType targetClass : vm.allClasses())
            if(targetClass.name().equals(className)){
                Location location =
                        null;
                try {
                    location = targetClass.locationsOfLine(lineNumber).get(0);
                } catch (AbsentInformationException e) {
                    throw new RuntimeException(e);
                }
                BreakpointRequest bpReq =
                    vm.eventRequestManager ( ).createBreakpointRequest(location);
            bpReq.enable();
        }
    }

    public void stepCommand(LocatableEvent event) {
        System.out.print("Command > ");
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();

        if (command.equals("step")) {
            enableStepRequests(event);
        }

    }
}
