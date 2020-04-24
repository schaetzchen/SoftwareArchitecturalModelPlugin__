package plugindata;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import plugindata.entities.ExecutionDataEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadLogAction extends AnAction {

    ArrayList<ExecutionDataEvent> events;

    private void readFromFile(Project project) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(project.getBasePath() + "/new.csv"));
            br.readLine();

            String row = br.readLine();
            while (row != null) {

                try {

                    String[] data = row.split(",");

                    Integer calleeID = data[1].equals("-") ? Integer.MIN_VALUE : Integer.parseInt(data[1]);
                    Integer callerID = data[3].equals("-") ? Integer.MIN_VALUE : Integer.parseInt(data[3]);
                    events.add(new ExecutionDataEvent(data[0].split("\\.")[1], calleeID,
                            data[2].split("\\.")[1], callerID, data[4]));
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    row = br.readLine();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            br.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        events = new ArrayList<>();
        readFromFile(e.getProject());
        int a = 54;
        int b = a + 3;
    }
}
