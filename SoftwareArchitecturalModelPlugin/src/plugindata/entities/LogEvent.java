package plugindata.entities;

import java.util.Date;

public class LogEvent {

    private Integer caseID;
    private String activityName, transactionType;
    private Date timestamp;

    public LogEvent(Integer caseID, String activityName, String transactionType, Date timestamp) {
        this.caseID = caseID;
        this.activityName = activityName;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }
}
