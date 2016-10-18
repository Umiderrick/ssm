/**
 * 
 */
package im.vinci.monitor;



/**
 * @author liuyue
 * 
 */
public class ThreadCpuUsageBean {
	
	private long lastCummulativeCpuTime;
	private long lastCummulativeUserTime;
	private long lastCummulativeSysTime;
	private double currentUserCpuUsage;
	private double currentSysCpuUsage;
	private double currentCpuUsage;

	public ThreadCpuUsageBean() {
	}
	/**
	 * 得到当前的cpu user usage, cpu sys usage, cpu usage，同步保证三个数值的一致性
	 * @return
	 */
	public synchronized double[] getCurrentCpuUsage() {
		return new double[] { currentUserCpuUsage, currentSysCpuUsage,
				currentCpuUsage };
	}
	/**
	 * 计算当前的cpu user usage, cpu sys usage, cpu usage，同步保证数据一致更新
	 * @param currCummulativeCpuTime
	 * @param currCummulativeUserTime
	 * @param currCummulativeSysTime
	 * @param timeSplit
	 */
	public synchronized void computeCpuUsage(final long currCummulativeCpuTime,
			final long currCummulativeUserTime,
			final long currCummulativeSysTime,
			final long timeSplit) {	
		currentCpuUsage = (currCummulativeCpuTime - lastCummulativeCpuTime)*1.0D/timeSplit;
		currentUserCpuUsage = (currCummulativeUserTime - lastCummulativeUserTime)*1.0D/timeSplit;
		currentSysCpuUsage = (currCummulativeSysTime - lastCummulativeSysTime)*1.0D/timeSplit;
		lastCummulativeCpuTime = currCummulativeCpuTime;
		lastCummulativeUserTime = currCummulativeUserTime;
		lastCummulativeSysTime = currCummulativeSysTime;
	}
}
