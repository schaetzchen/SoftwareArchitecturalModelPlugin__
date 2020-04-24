package plugindata.component_identification;

import com.intellij.openapi.project.Project;
import plugindata.entities.ExecutionDataEvent;
import plugindata.entities.Graph;
import plugindata.RunnableHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ComponentDataHandler {

    public static Set<String> packageData;

    public static ComponentExecutionData handleDataProjection(String componentName, ArrayList<ExecutionDataEvent> executionData) {

        ArrayList<ExecutionDataEvent> componentExecData = new ArrayList<>();

        for (ExecutionDataEvent event : executionData)
            if (event.getCalleeMethod().substring(0, event.getCalleeMethod().lastIndexOf('.')).equals(componentName))
                componentExecData.add(event);

        return new ComponentExecutionData(componentName, componentExecData);
    }

    public static void getPackageData(Project project) {

        RunnableHelper.runReadCommand(project, () -> packageData = RunnableHelper.getPackageData(project));
    }

    private static boolean checkMatrixEdge(ArrayList<ExecutionDataEvent> executionData, Integer o1, Integer o2) {

        for (ExecutionDataEvent event : executionData)
            if (event.getCallerID().intValue() == o1.intValue() && event.getCalleeID().intValue() == o2.intValue())
                return true;

        return false;
    }

    public static Set<ArrayList<Integer>> getComponentInstances(ArrayList<ExecutionDataEvent> executionData) {

        ArrayList<Integer> objects = new ArrayList<>();
        for (ExecutionDataEvent event : executionData)
            if (event.getCalleeID() != Integer.MIN_VALUE)
                objects.add(event.getCalleeID());

        Graph g = new Graph(objects.size());

        for (int i = 0; i < objects.size(); i++)
            for (int j = 0; j < objects.size(); j++)
                if (checkMatrixEdge(executionData, objects.get(i), objects.get(j))) {
                    g.addEdge(i, j);
                    g.addEdge(j, i);
                }

        Set<ArrayList<Integer>> res = new HashSet<>();

        for (ArrayList<Integer> instanceIndices : g.connectedComponents()) {
            ArrayList<Integer> instance = new ArrayList<>();
            for (Integer o : instanceIndices)
                instance.add(objects.get(o.intValue()));
            res.add(instance);
        }

        return res;
    }
}
