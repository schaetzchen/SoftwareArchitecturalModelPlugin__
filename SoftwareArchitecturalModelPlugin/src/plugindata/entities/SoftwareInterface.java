package plugindata.entities;

import java.util.Set;

public class SoftwareInterface {

    private String name;
    private String belongingComponentName;
    private Set<String> methodNames;

    public SoftwareInterface(String name, String belongingComponentName, Set<String> methodNames) {
        this.name = name;
        this.belongingComponentName = belongingComponentName;
        this.methodNames = methodNames;
    }

    public String getName() {
        return name;
    }

    public String getBelongingComponentName() {
        return belongingComponentName;
    }

    public Set<String> getMethodNames() {
        return methodNames;
    }

    public boolean containsMethod(String method)  {

        for (String s : methodNames)
            if (s.equals(method))
                return true;

        return false;
    }
}
