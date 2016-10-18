package im.vinci.core.resource;

/**
 * ResourceHandler
 * 
 * @author HuangYunHui|XiongChun
 * @since 2009-11-20
 */
public interface ResourceHandler {
	void handle(Resource pResource) throws HandleResourceException;
}
