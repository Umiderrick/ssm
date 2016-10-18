/**
 * Project Name:jhorse_core
 * File Name:IdUtil.java
 * Package Name:org.jhorse.core.util
 * Date:2015年6月28日上午10:44:35
 * Copyright (c) 2015, mlc0202@126.com All Rights Reserved.
 *
*/

package im.vinci.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ClassName:IdUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 一个简易的id生成器. <br/>
 * Date:     2015年6月28日 上午10:44:35 <br/>
 * @author   mlc
 * @version  
 * @see 	 
 */
public class IdUtil {
	//随机字符串
		public static String getRandString(int length) {
			StringBuffer s = new StringBuffer("" + (new Date().getTime()));
			Random r = new Random(10);
			s.append(Math.abs(r.nextInt()));
			if (s.toString().length() > length)
				s.substring(0, length);
			return s.toString();
		}
		//随机20位字符串ID
		public static String getOnlyID() {
			String strRnd;
			Long dblTmp;
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			// ---- 7random
			dblTmp = (long) (Math.random() * 100000);
			while (dblTmp < 10000) {
				dblTmp = (long) (Math.random() * 100000);
			}
			strRnd = String.valueOf(dblTmp).substring(0, 3);
			String s = df.format(new Date()) + strRnd;
			return s;
		}
		
		public static void main(String[] args){
			System.out.println(getOnlyID());
		}
}
