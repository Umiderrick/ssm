package im.vinci.core.resource.support;

import im.vinci.core.resource.ResourceException;

/**
 * ResourceConfigMapping
 * 
 * @author HuangYunHui|XiongChun
 * @since 2010-2-5
 */
public interface ResourceConfigMapping {
	ResourceConfig mapping(String pUri) throws ResourceException;
}
