package im.vinci.core.resource.support;

import im.vinci.core.resource.ResourceException;
import im.vinci.core.resource.ResourceLoader;

/**
 * LoaderMapping
 * 
 * @author HuangYunHui|XiongChun
 * @since 2010-2-5
 */
public interface LoaderMapping {
	ResourceLoader mapping(String pName) throws ResourceException;
}
