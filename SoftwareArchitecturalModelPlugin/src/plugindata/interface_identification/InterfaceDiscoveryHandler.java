package plugindata.interface_identification;

import com.intellij.openapi.util.Pair;
import plugindata.entities.ExecutionDataEvent;
import plugindata.component_identification.ComponentExecutionData;
import plugindata.entities.SoftwareExecutionData;
import plugindata.entities.SoftwareInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class InterfaceDiscoveryHandler {

    static Set<Pair<String, String>> getTopLevelMethodSetOfAComponent(ComponentExecutionData SD) {

        // First element in a pair is the name of a caller method, second is the name of a callee method
        Set<Pair<String, String>> topLevelMethods = new HashSet<>();

        for (ExecutionDataEvent event : SD.getExecutionData()) {
            if (!event.getCallerMethod().substring(0, event.getCallerMethod().lastIndexOf(".")).equals(SD.getComponentName()))
                topLevelMethods.add(new Pair<>(event.getCallerMethod(), event.getCalleeMethod()));
        }

        return topLevelMethods;
    }

    static Set<Set<String>> getCandidateInterfaceSet(Set<Pair<String, String>> topLevelMethodSet) {

        Set<Pair<String, Set<String>>> tempSet = new HashSet<>();

        for (Pair<String, String> p : topLevelMethodSet)
            handleSetOfPairs(tempSet, p);

        Set<Set<String>> res = new HashSet<>();
        for (Pair<String, Set<String>> p : tempSet)
            res.add(p.second);

        return res;
    }

    private static Double getCandidateInterfaceSimilarity(Set<String> first, Set<String> second) {

        Integer numberOfCommonMethods = 0;
        for (String s1 : first)
            for (String s2 : second)
                if (s1.equals(s2))
                    numberOfCommonMethods++;

        return numberOfCommonMethods.doubleValue() / (first.size() + second.size());
    }

    static List<Set<String>> mergeInterfaces(Set<Set<String>> candidates, Double threshold) {

        Double similarity = 1.;

        ArrayList<Set<String>> list = new ArrayList<>();
        list.addAll(candidates);
        while (similarity >= threshold) {

            boolean keepgoing = true;
            for (int i = 0; i < list.size() - 1 && keepgoing; i++)
                for (int j = i + 1; j < list.size() && keepgoing; j++) {

                    similarity = getCandidateInterfaceSimilarity(list.get(i), list.get(j));
                    if (similarity >= threshold) {

                        list.add(mergeTwoCandidates(list.get(i), list.get(j)));
                        list.remove(j);
                        list.remove(i);
                        keepgoing = false;
                    }
                }
        }

        return list;
    }

    private static Set<String> mergeTwoCandidates(Set<String> first, Set<String> second) {

        Set<String> res = new HashSet<>();
        res.addAll(first);
        res.addAll(second);
        return res;
    }

    private static void handleSetOfPairs(Set<Pair<String, Set<String>>> set, Pair<String, String> newPair) {

        for (Pair<String, Set<String>> el : set)
            if (el.first.equals(newPair.first)) {
                el.second.add(newPair.second);
                return;
            }

        Set<String> newSet = new HashSet<>();
        newSet.add(newPair.second);
        set.add(new Pair<>(newPair.first, newSet));
    }

    public Set<ExecutionDataEvent> getInterfaceExecutionData(SoftwareInterface anInterface, SoftwareExecutionData data) {

        Set<ExecutionDataEvent> res = new HashSet<>();

        for (ExecutionDataEvent event : data.getData()) {

            if (anInterface.containsMethod(event.getCalleeMethod()) &&
            !event.getCallerMethod().substring(0, event.getCallerMethod().lastIndexOf(".")).
                    equals(anInterface.getBelongingComponentName()))
                res.add(event);
        }

        return res;
    }
}