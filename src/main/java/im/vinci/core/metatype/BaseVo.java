package im.vinci.core.metatype;

import java.io.Serializable;

import im.vinci.core.metatype.impl.BaseDto;
import im.vinci.core.util.G4Utils;

/**
 * 简单值对象<br>
 * 简单值对象和数据库表没有一一对应关系<br>
 * <b>注意：没有特殊需要建议不用VO,对于固定的数据存储结构请使用Domain;对于动态可变的
 * 的数据存储结构请使用Dto来代替.
 * @author OSWorks-XC
 * @since 2009-06-23
 * @see Serializable
 */
public abstract class BaseVo implements Serializable{
	
    /**
     * 将值对象转换为Dto对象
     * 
     * @return dto 返回的Dto对象
     */
	public Dto toDto(){
		Dto dto = new BaseDto();
		G4Utils.copyPropFromBean2Dto(this, dto);
		return dto;
	}

	/**
	 * 将值对象转换为JSON字符串
	 * @return String 返回的JSON格式字符串
	 */
    public String toJson(){
    	Dto dto = toDto();
    	return dto.toJson();
    }
}
