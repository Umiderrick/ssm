package im.vinci.core.id;

/**
 * SequenceGenerator 此代码源于开源项目E3,原作者：黄云辉
 * 
 * @author OSWorks-XC
 * @since 2010-03-17
 */
public interface SequenceGenerator {
	long next() throws CreateSequnceException;
}
