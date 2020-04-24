package plugindata.entities;

public class ExecutionDataEvent {

    private String calleeMethod, callerMethod;
    private Integer calleeID, callerID;
    private String date;
    private Integer caseID;

    public ExecutionDataEvent(String calleeMethod, Integer calleeID, String callerMethod, Integer callerID,
                              String date, Integer caseID) {

        this.calleeMethod = calleeMethod;
        this.calleeID = calleeID;
        this.callerMethod = callerMethod;
        this.callerID = callerID;
        this.date = date;
        this.caseID = caseID;
    }

    public String getCalleeMethod() {
        return calleeMethod;
    }

    public Integer getCalleeID() {
        return calleeID;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public Integer getCallerID() {
        return callerID;
    }

    public String getDate() {
        return date;
    }

    public Integer getCaseID() {
        return caseID;
    }
}