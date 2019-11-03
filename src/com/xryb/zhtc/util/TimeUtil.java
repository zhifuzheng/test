package com.xryb.zhtc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
	
	/** 获得 两个日期之间所有日期集合  含开始日期和结束日期
	 * @param stateTime 开始日期
	 * @param endTime  结束日期
	 * @return
	 *2019年1月28日
	 *@author wangchen
	 */
	public static List<Date> getAllDate(Date stateTime,Date endTime){
			
			List<Date> list = new ArrayList<Date>();
			Calendar state = Calendar.getInstance();
			state.setTime(stateTime);
			Calendar end = Calendar.getInstance();
			end.setTime(endTime);	
			list.add(stateTime);
			while (end.getTime().after(state.getTime())) {
				state.add(Calendar.DAY_OF_MONTH, 1);
				list.add(state.getTime());
			}
			return list;
		}
	 	
		/**  将日期集合转化成日期字符串集合
		 * @param list 日期集合
		 * @return
		 *2019年1月28日
		 *@author wangchen
		 */
		public static List<String> getAllDateString(List<Date> list){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<String> dateList = new ArrayList<>();
			for(int i=0;i<list.size();i++) {
				dateList.add(sdf.format(dateList.get(i)));
			}
			return dateList;
		}
		
		
		/**  获得大关卡日期所在星期的星期一和星期天
		 * @param date 当前日期
		 * @return
		 *2019年1月28日
		 *@author wangchen
		 */
		public static String getTimeInterval(Date date) {  
	   	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	       Calendar cal = Calendar.getInstance();  
	       cal.setTime(date);  
	       // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
	       int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
	       if (1 == dayWeek) {  
	          cal.add(Calendar.DAY_OF_MONTH, -1);  
	       }  
	       // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期  
	       // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
	       cal.setFirstDayOfWeek(Calendar.MONDAY);  
	       // 获得当前日期是一个星期的第几天  
	       int day = cal.get(Calendar.DAY_OF_WEEK);  
	       // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
	       cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
	       String imptimeBegin = sdf.format(cal.getTime());  
	       // System.out.println("所在周星期一的日期：" + imptimeBegin);  
	       cal.add(Calendar.DATE, 6);  
	       String imptimeEnd = sdf.format(cal.getTime());  
	       // System.out.println("所在周星期日的日期：" + imptimeEnd);  
	       return imptimeBegin + "," + imptimeEnd;  
	  }
}
