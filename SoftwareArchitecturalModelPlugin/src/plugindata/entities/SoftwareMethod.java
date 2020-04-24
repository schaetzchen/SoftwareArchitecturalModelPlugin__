package plugindata.entities;

import plugindata.entities.SoftwareInterface;

public class SoftwareMethod {

    private String methodName;
    private SoftwareInterface belongingInterface;

    public SoftwareMethod(String methodName, SoftwareInterface belongingInterface) {
        this.methodName = methodName;
        this.belongingInterface = belongingInterface;
    }

    public String getMethodName() {
        return methodName;
    }

    public SoftwareInterface getBelongingInterface() {
        return belongingInterface;
    }

    public String getBelongingComponent() {
        return belongingInterface.getBelongingComponentName();
    }
}