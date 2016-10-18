package im.vinci.core.resource.support;

import im.vinci.core.resource.ResourceException;
import im.vinci.core.resource.ResourceHandler;

/**
 * HandlerMapping
 * 
 * @author HuangYunHui|XiongChun
 * @since 2010-2-5
 */
public interface HandlerMapping {

	ResourceHandler mapping(String pName) throws ResourceException;

}
