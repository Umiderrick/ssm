package im.vinci.core.resource.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import im.vinci.core.resource.Cache;
import im.vinci.core.resource.ResourceHandler;
import im.vinci.core.resource.ResourceLoader;
import im.vinci.core.resource.ResourceManager;
import im.vinci.core.resource.cache.MemoryCache;
import im.vinci.core.resource.support.HandlerMapping;
import im.vinci.core.resource.support.LoaderMapping;
import im.vinci.core.resource.support.ResourceConfigMapping;
import im.vinci.core.resource.util.StringUtils;

/**
 * Configuration
 * 
 * @author HuangYunHui|XiongChun
 * @since 2009-06-10
 */
public class Configuration {

	private final static String DEFAULT_RESOURCE = "";

	private final Log logger = LogFactory.getLog(Configuration.class);

	private Properties properites = null;

	public Configuration() {
		properites = new Properties();
	}

	public void build() throws ConfigeException {
		this.buildResource(DEFAULT_RESOURCE);
	}

	public void buildFile(String pFile) throws ConfigeException {
		FileInputStream fileIS = null;
		try {
			fileIS = new FileInputStream(pFile);
		} catch (FileNotFoundException e) {
			final String MSG = "打开资源文件:" + pFile + "失败!";
			logger.error(MSG, e);
			throw new ConfigeException(MSG, e);
		}
		BufferedInputStream bufferedIS = new BufferedInputStream(fileIS);
		try {
			buildInputStream(bufferedIS);
		} finally {
			try {
				fileIS.close();
				bufferedIS.close();
			} catch (IOException e) {
				final String MSG = "关闭资源文件:" + pFile + "失败!";
				logger.warn(MSG, e);
			}

		}
	}

	public void buildResource(String pResource) throws ConfigeException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(pResource);
		BufferedInputStream bufferedIS = new BufferedInputStream(is);
		try {
			buildInputStream(bufferedIS);
		} finally {
			try {
				is.close();
				bufferedIS.close();
			} catch (IOException e) {
				final String MSG = "关闭资源文件:" + pResource + "失败!";
				logger.warn(MSG, e);
			}
		}
	}

	public void buildProperties(Properties pProperties) throws ConfigeException {
		properites.clear();
		properites.putAll(pProperties);
	}

	public void buildInputStream(InputStream pIS) throws ConfigeException {
		properites.clear();
		try {
			properites.load(pIS);
		} catch (IOException e) {
			final String MSG = "读取配置文件失败!";
			logger.error(MSG, e);
			throw new ConfigeException(MSG, e);
		}
	}

	private static final String CACHE_KEY = "resource.cache";
	private static final String LOADER_KEY = "resource.loaders";
	private static final String HANDLER_KEY = "resource.handlers";
	private static final String MAPPING_KEY = "resource.uriMappings";
	private static final String CHECK_MODIFIED_KEY = "resouce.checkModified";

	private boolean isEmpty(String pValue) {
		if (pValue == null) {
			return true;
		}
        return "".equals(pValue.trim());
    }

	private static class ObjectFactory {
		public static Object getObject(String pClass) {
			/**
			 * @TODO: 上网看看别人怎么处理?
			 */
			try {
				return Class.forName(pClass).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("创建类对象失败!" + pClass, e);
			}
		}
	}

	private static final String CLASS_POSTFIX = "class";

	private void setProprty(Object pObj, String pProperty, String pValue) throws ConfigeException {
		try {
			BeanUtils.setProperty(pObj, pProperty, pValue);
		} catch (Exception ex) {
			final String MSG = "设置对象:" + pObj.getClass().getName() + "的" + pProperty + " 属性为" + pValue + "时出现异常";
			logger.error(MSG, ex);
			throw new ConfigeException(MSG, ex);
		}
	}

	private Object createObject(String pObjectKeyPrefix, Class pDestClass) throws ConfigeException {
		final String classKey = pObjectKeyPrefix + CLASS_POSTFIX;
		final String className = properites.getProperty(classKey);
		if (className == null) {
			final String MSG = "配置文件中没有定义属性：" + classKey + ",请仔细检查配置文件！";
			logger.error(MSG);
			throw new ConfigeException(MSG);
		}
		Object obj = ObjectFactory.getObject(className);
		if (pDestClass.isInstance(obj) == false) {
			throw new ConfigeException(className + "未实现接口:" + pDestClass.getName());
		}
		Map properties = getObjectProperties(pObjectKeyPrefix);
		Iterator propertiesIterator = properties.keySet().iterator();
		while (propertiesIterator.hasNext()) {
			String property = (String) propertiesIterator.next();
			String value = (String) properties.get(property);
			setProprty(obj, property, value);
		}

		return obj;
	}

	/**
	 * key 是属性名称 value是属性值
	 * 
	 * @param pObjectKeyPrefix
	 * @return
	 */
	private Map getObjectProperties(String pObjectKeyPrefix) {
		Map result = new LinkedHashMap();
		Iterator keyIterator = properites.keySet().iterator();
		final String classKey = pObjectKeyPrefix + CLASS_POSTFIX;
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			boolean isClassKey = key.startsWith(classKey);
			if (isClassKey) {
				continue;
			}
			if (key.startsWith(pObjectKeyPrefix) == false) {
				continue;
			}
			String value = properites.getProperty(key);
			result.put(key.substring(pObjectKeyPrefix.length()), value);
		}
		return result;
	}

	/**
	 * 创建cache
	 * 
	 * @return
	 * @throws ConfigeException
	 */
	private Cache createCache() throws ConfigeException {
		String cacheName = properites.getProperty(CACHE_KEY);
		if (isEmpty(cacheName)) {
			logger.info("没有发现cache配置，采用默认的MemoryCache");
			return new MemoryCache();
		}
		return (Cache) this.createObject(cacheName + ".", Cache.class);
	}

	/**
	 * 将字符串变成数组,分割符是,或者;
	 * 
	 * @param pStr
	 * @return
	 */
	private String[] toArray(String pStr) {
		if (pStr == null) {
			return new String[0];
		}
		return StringUtils.tokenizeToStringArray(pStr, Constants.SPLITER);
	}

	/**
	 * 创建ResourceLoader对象
	 * 
	 * @return
	 * @throws ConfigeException
	 */
	private LoaderMapping createLoaderMapping() throws ConfigeException {
		DefaultLoaderMapping result = new DefaultLoaderMapping();
		String loaderNames = properites.getProperty(LOADER_KEY);
		if (isEmpty(loaderNames)) {
			return result;
		}
		String[] loaderNameArray = toArray(loaderNames);
		for (int i = 0; i < loaderNameArray.length; i++) {
			String loaderName = loaderNameArray[i];
			result.put(loaderName, (ResourceLoader) this.createObject(loaderName + ".", ResourceLoader.class));
		}
		return result;
	}

	private HandlerMapping createHandlerMapping() throws ConfigeException {
		DefaultHandlerMapping result = new DefaultHandlerMapping();
		String handlerNames = properites.getProperty(HANDLER_KEY);
		if (isEmpty(handlerNames)) {
			return result;
		}
		String[] handlerNameArray = toArray(handlerNames);
		for (int i = 0; i < handlerNameArray.length; i++) {
			String handlerName = handlerNameArray[i];
			result.put(handlerName, (ResourceHandler) this.createObject(handlerName + ".", ResourceHandler.class));
		}
		return result;
	}

	private static final String URI_PATTERN = "*.uriPattern";
	private static final String INCLUDE_PATTERN = "*.includes";

	private String getUriMappingName(String pKey) {
		int index = pKey.indexOf(".");
		if (index == -1) {
			return null;
		}
		return pKey.substring(0, index);
	}

}
