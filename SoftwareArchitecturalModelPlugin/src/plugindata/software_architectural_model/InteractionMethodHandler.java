package plugindata.software_architectural_model;

import plugindata.entities.Graph;
import plugindata.entities.SoftwareExecutionData;
import plugindata.entities.SoftwareMethod;
import plugindata.entities.SoftwareInterface;

import java.util.HashSet;
import java.util.Set;

public abstract class InteractionMethodHandler {

    public static Graph getMethodCallingGraph(SoftwareExecutionData executionData) {

        Graph g = new Graph(executionData.getData().size());

        for (int i = 0; i < executionData.getData().size() - 1; i++)
            for (int j = i + 1; j < executionData.getData().size(); j++)
                if (!executionData.getData().get(i).getCalleeID().equals(Integer.MIN_VALUE) &&
                executionData.getData().get(i).getCalleeID().equals(executionData.getData().get(j).getCallerID()))
                    g.addEdge(i, j);

        return g;
    }

    public static boolean checkInvocationRelation(int v1, int v2, Graph g) {

        return g.DFSUtil(v1, v2, new boolean[g.getNumberOfVertices()]);
    }

    public static SoftwareInterface getBelongingInterface(Set<SoftwareInterface> interfaces, String methodName) {

        for (SoftwareInterface anInterface : interfaces) {
            for (String s : anInterface.getMethodNames())
                if (methodName.equals(s))
                    return anInterface;
        }

        return null;
    }

    public static Set<SoftwareMethod> getInteractionMethods(Set<SoftwareInterface> interfaces,
                                                     SoftwareExecutionData data, Graph methodCallingGraph) {

        Set<SoftwareMethod> res = new HashSet<>();

        for (SoftwareInterface anInterface : interfaces) {
            for (String candidateMethod : anInterface.getMethodNames()) {

                boolean keepgoing = true;

                for (int i = 0; i < data.getData().size() - 1 && keepgoing; i++) {
                    for (int j = i; j < data.getData().size() && keepgoing; j++) {

                        if (candidateMethod.equals(data.getData().get(i).getCalleeMethod())) {
                            SoftwareInterface secondMethodInterface =
                                    getBelongingInterface(interfaces, data.getData().get(j).getCalleeMethod());

                            if (secondMethodInterface != null &&
                            secondMethodInterface.getBelongingComponentName().equals(anInterface.getBelongingComponentName()) &&
                            checkInvocationRelation(i, j, methodCallingGraph)) {
                                res.add(new SoftwareMethod(candidateMethod, anInterface));
                                keepgoing = false;
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
}