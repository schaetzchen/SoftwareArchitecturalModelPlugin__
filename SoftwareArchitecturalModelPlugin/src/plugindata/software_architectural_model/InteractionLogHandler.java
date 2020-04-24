package plugindata.software_architectural_model;

import plugindata.entities.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InteractionLogHandler {

    private SoftwareMethod interactionMethod;
    private Log eventLog;
    private List<Integer> softwareRuns;
    private List<Set<ExecutionDataEvent>> runMethodCalls;

    public InteractionLogHandler(SoftwareMethod interactionMethod, SoftwareExecutionData executionData) {
        this.interactionMethod = interactionMethod;
        eventLog = new Log();
        softwareRuns = new ArrayList<>();
        countSoftwareRuns(executionData);
        initRunMethodCalls(executionData);
        createLog(executionData);
    }

    private void createLog(SoftwareExecutionData executionData) {

        for (Set<ExecutionDataEvent> events : runMethodCalls)
            for (ExecutionDataEvent event : events) {

                Integer caseID = event.getCaseID();
//                String activityName = InteractionMethodHandler.getBelongingInterface(interfaces, event.getCalleeMethod());
                String activityName = "fix_this_name";
                String transactionTypeStart = "start";
                String transactionTypeComplete = "complete";
                // todo implement
                String timestampStart, timestampComplete;

                eventLog.addEvent(new LogEvent(caseID, activityName, transactionTypeStart, timestampStart));
                eventLog.addEvent(new LogEvent(caseID, activityName, transactionTypeComplete, timestampComplete));
            }

//        for (int i = 0; i < softwareRuns.size(); i++) {
//
//            Integer caseID = softwareRuns.get(i);
//            String activityName = InteractionMethodHandler.getBelongingInterface(interfaces, )
//        }
    }

    private void countSoftwareRuns(SoftwareExecutionData executionData) {

        for (ExecutionDataEvent event : executionData.getData())
            if (event.getCalleeMethod().equals(interactionMethod.getMethodName()))
                softwareRuns.add(event.getCaseID());
    }

    private void initRunMethodCalls(SoftwareExecutionData executionData) {

        runMethodCalls  = new ArrayList<>();

        for (Integer runID : softwareRuns) {
            Set<ExecutionDataEvent> runEvents = new HashSet<>();
            for (ExecutionDataEvent event : executionData.getData()) {
                if (event.getCaseID().equals(runID))
                    runEvents.add(event);
            }
            runMethodCalls.add(runEvents);
        }
    }
}
