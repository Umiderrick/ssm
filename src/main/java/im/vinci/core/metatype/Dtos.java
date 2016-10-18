/**
 * Project Name:jhorse_core
 * File Name:Dtos.java
 * Package Name:im.vinci.core.metatype
 * Date:2015年6月27日下午10:26:47
 * Copyright (c) 2015, mlc0202@126.com All Rights Reserved.
 *
*/

package im.vinci.core.metatype;

import im.vinci.core.WebUtils.WebUtils;
import im.vinci.core.metatype.impl.BaseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ClassName:Dtos <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年6月27日 下午10:26:47 <br/>
 * @author   mlc
 * @version  
 * @see 	 
 */
public class Dtos {
	 public static Dto newDto() {
	        return new BaseDto();
	    }

	    public static Dto newOutDto() {
	        Dto outDto = newDto();
	        return outDto;
	    }

	    public static Dto newDto(String keyString, Object valueObject) {
	        Dto dto = new BaseDto();
	        dto.put(keyString, valueObject);
	        return dto;
	    }

	    public static Dto newDto(HttpServletRequest request) {
	        return WebUtils.getParamAsDto(request);
	    }

		public static Dto fromJson(HttpServletRequest request)throws Exception{
			return WebUtils.getJsonAsDto(request);
		}

		public static List<Dto> newDtosList(HttpServletRequest request)throws Exception{
			return WebUtils.getJsonArrayAsDtos(request);
		}

}
