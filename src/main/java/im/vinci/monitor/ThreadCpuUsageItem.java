/**
 * 
 */
package im.vinci.monitor;

/**
 * @author liuyue
 *
 */
public class ThreadCpuUsageItem {
	private final String threadName;
	private final long threadId;
	private final double[] usages;
	
	public ThreadCpuUsageItem(String threadName, long threadId, double[] usages){
		this.threadId = threadId;
		this.threadName = threadName;
		this.usages = usages;
	}
	
	public double[] getUsages(){
		return usages;
	}
	
	public String getThreadName(){
		return threadName;
	}
	
	public long getThreadId(){
		return threadId;
	}
}
