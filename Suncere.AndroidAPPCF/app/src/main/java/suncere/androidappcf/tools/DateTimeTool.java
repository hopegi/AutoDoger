package suncere.androidappcf.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.util.Log;

public class DateTimeTool {
	static SimpleDateFormat fullf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat tf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
//	static SimpleDateFormat tf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar cal=Calendar.getInstance();
	
	static List<SimpleDateFormat> formatLst=  Arrays.asList( new SimpleDateFormat[]{fullf,tf,datef,timef});
	
	public static Date GetNow()
	{
		return new Date();
	}
	
	public static String GetNowDateString()
	{
		return datef.format(GetNow());
	}
	
	public static String GetNowString()
	{
		return tf.format(GetNow());
	}
	
	public static String ToString(Date date)
	{
		return tf.format(date);
	}
	
	public static String ToString(Date date,String format)
	{
		SimpleDateFormat sdt=new SimpleDateFormat(format);
		return sdt.format(date);
	}
	
	public static String ToDateString(Date date)
	{
		return datef.format(date);
	}
	
	public static String ToTimeString(Date date)
	{
		return timef.format(date);
	}
	
	public static float TotalMinutes(Date startDate,Date endDate)
	{
		return TotalMinutes(  tf.format(startDate),tf.format(endDate));
	}
	
	public static float TotalMinutes(String startDate,String endDate)
	{
		return TotalMinutesCore(startDate,endDate);
	}
	
	public static float TotalMinutes(Object startDate,Object endDate)
	{
			float result=-999;
			boolean errFlag=true;
			
			if(startDate instanceof String || startDate instanceof Date )
				errFlag=false;
			if(!errFlag&&(endDate instanceof String || endDate instanceof Date ))
				errFlag=false;
			
			if(errFlag)return result;
			
			return TotalMinutes(Prase(startDate),Prase(endDate));
			
	}
	
	private static float TotalMinutesCore(String startDate,String endDate)
	{
		float result=-999;
		try{
			long startT= fromDateStringToLong(startDate);
			long endT=fromDateStringToLong(endDate);
			result=(endT-startT)/(1000*60.0f);
		}catch(Exception ex)
		{
			ex.printStackTrace(); 
		}
		return result;
	}
	
	public static float TotalSeconds(Date startDate,Date endDate)
	{
		return TotalSeconds(  fullf.format(startDate),fullf.format(endDate));
	}
	
	public static float TotalSeconds(String startDate,String endDate)
	{
		return TotalSecondsCore(startDate,endDate);
	}
	
	public static float TotalSeconds(Object startDate,Object endDate)
	{
			float result=-999;
			boolean errFlag=true;
			
			if(startDate instanceof String || startDate instanceof Date )
				errFlag=false;
			if(!errFlag&&(endDate instanceof String || endDate instanceof Date ))
				errFlag=false;
			
			if(errFlag)return result;
			
			return TotalSeconds(Prase(startDate),Prase(endDate));
			
	}
	
	private static float TotalSecondsCore(String startDate,String endDate)
	{
		float result=-999;
		try{
			long startT= fromDateStringToLong(startDate);
			long endT=fromDateStringToLong(endDate);
			result=(endT-startT)/(1000f);
		}catch(Exception ex)
		{
			ex.printStackTrace(); 
		}
		return result;
	}
	
	public static Date Prase(Object value)
	{
		Date result=null;
		if(value instanceof String){
//				result=tf.parse(value.toString());
				String valStr=value.toString().replace("T", " ");
//				Log.d("Tag"," DBTime "+ valStr);
				for(SimpleDateFormat sdf :formatLst)
				{
					if(result!=null)break;
					try{
						result=sdf.parse(valStr);
					}
					catch(Exception ex)
					{
						
					}
				}
//				result=tf.parse(valStr);
		}
		else if(value instanceof Date)
			result=(Date)value;
		
		return result;
	}

	public static Date AddDays(Date date,int days)
	{
		cal.setTime(date);
		cal.add(cal.DATE, days);
		return cal.getTime();
	}
	
	public static Date AddHours(Date date,int hours)
	{
		cal.setTime(date);
		cal.add(cal.HOUR, hours);
		return cal.getTime();
	}
	
	public static Date AddMinutes(Date date,int minutes)
	{
		cal.setTime(date);
		cal.add(cal.MINUTE, minutes);
		return cal.getTime();
	}
	
	public static Date AddMonths(Date date,int months)
	{
		cal.setTime(date);
		cal.add(cal.MONTH, months);
		return cal.getTime();
	}
	
	public static int GetYear(Date date)
	{
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	public static int GetHour(Date date)
	{
		cal.setTime(date);
		return cal.get(Calendar.HOUR);
	}
	public static int GetDay(Date date)
	{
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}
	public static int GetMonth(Date date)
	{
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}
	
	public static int GetDayOfYear(Date date)
	{
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	public static int GetDayOfMonth(Date date)
	{
		cal.setTime(date);
		return date.getDay();
	}
	
	public static int GetSeason(Date date)
	{
		int month=date.getMonth();
		if(month%3==0)return month/3;
		return month/3+1;
	}
	
	public static int GetWeekOfYear(Date date)
	{
		Calendar cal =  Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int GetWeekOfMonth(Date date)
	{
		Calendar cal =  Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}
	
	private static long fromDateStringToLong(String inVal) { //此方法计算时间毫秒 
//		Date date = null; //定义时间类型 
////		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:ss:"); 
//		try { 
//			date = fullf.parse(inVal); //将字符型转换成日期型 
//		} catch (Exception e) { 
//		e.printStackTrace(); 
//		} 
//		return date.getTime(); //返回毫秒数 
		return fromDateStringToLong(inVal,formatLst.get(0));
	}
	
	private static long fromDateStringToLong(String inVal,SimpleDateFormat format)
	{
		Date date =null;
		try
		{
			date=format.parse(inVal);
		}
		catch(Exception ex)
		{
			int index=formatLst.indexOf(format)+1;
			if(index==formatLst.size())return -999;
			 return fromDateStringToLong(inVal,formatLst.get(index));
		}
		return date.getTime();
	}
	
	private static String dqsj() { //此方法用于获得当前系统时间（格式类型2007-11-6 15:10:58） 
		Date date = new Date(); //实例化日期类型 
		String today = date.toLocaleString(); //获取当前时间 
		System.out.println("获得当前系统时间 "+today); //显示 
		return today; //返回当前时间 
		}

}
