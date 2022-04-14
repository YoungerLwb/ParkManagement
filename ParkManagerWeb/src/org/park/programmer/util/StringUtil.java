package org.park.programmer.util;
/*
 * String类的一些共用操作方法
 */
public class StringUtil {
	public static boolean isEmpty(String str) {
		if(str == null || "".equals(str))
		{
			return true;
		}else {
			return false;
		}
	}
}
