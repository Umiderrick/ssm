package im.vinci.core.resource;

/**
 * Cache
 * 
 * @author HuangYunHui|XiongChun
 * @since 2009-11-01
 */
public interface Cache {

	/**
	 * 初始化cache
	 * 
	 * @throws CacheException
	 */
    void init() throws CacheException;

	/**
	 * 保存数据到cache
	 * 
	 * @param key
	 * @param pValue
	 * @throws CacheException
	 */
    void put(Object key, Object pValue) throws CacheException;

	/**
	 * 从cache中获取数据，当cache中不存在指定key时，返回null
	 * 
	 * @param key
	 * @return
	 * @throws CacheException
	 */
    Object get(Object key) throws CacheException;

	/**
	 * 从cache中删除数据
	 * 
	 * @param key
	 * @throws CacheException
	 */
    void remove(Object key) throws CacheException;

	/**
	 * 清空cache
	 * 
	 * @throws CacheException
	 */
    void clear() throws CacheException;

	/**
	 * 如果在init做了初始化操作，就可能需要在进行clean操作
	 * 
	 * @throws CacheException
	 */
    void destroy() throws CacheException;
}
