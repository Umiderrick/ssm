
package im.vinci.monitor;

/**
 * @author sunli
 */
public abstract class ComputerMonitor extends MonitorData {
    private String instanceName;
    /**
     * @return
     */
    @Override
    public final String getInstanceName() {
        return instanceName;
    }

    /**
     * @param instanceName
     */
    public final void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

}
