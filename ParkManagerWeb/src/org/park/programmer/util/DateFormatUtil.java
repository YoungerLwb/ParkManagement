package org.park.programmer.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
	public static String getFormDate(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
		
	}
}
