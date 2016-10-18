
package im.vinci.monitor;

/**
 * @author  sunli
 */
public interface Monitor {
    /**
     * 获取监控对象的名称
     * @return
     */
    String getInstanceName();
    /**
     * 设置监控对象的描述信息
     * @param  description
     */
    void setDescription(String description) ;
    /**
     * 获取监控對象的监控描述信息
     * @return
     */
    String getDescription();
    /**
     * 获取监控對象的监控数据
     * @return
     */
    Number getValue();

}
