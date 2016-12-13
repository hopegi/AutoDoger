package suncere.androidappcf.aqi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import suncere.androidappcf.controls.SuncereApplication;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

public class AQITool 
{
	protected static int[] AQIRange=new int[]{0,50,100,150,200,300,Integer.MAX_VALUE};
	
	/// 1,2,3,4,5,6 个等级
	protected static List<Integer> AirQueryNumberLst=new ArrayList<Integer>( Arrays.asList(1,2,3,4,5,6));
	
	///罗马数字等级
	protected static List<String> AirQueryRomaLevelLst=new ArrayList<String>(Arrays.asList("Ⅰ级","Ⅱ级","Ⅲ级","Ⅳ级","Ⅴ级","Ⅵ级","--"));
	
	//中文数字等级
	protected static List<String> AirQueryChineseNumberLevelLst=new ArrayList<String>(Arrays.asList("一级","二级","三级","四级","五级","六级","--"));
	
	///等级
	protected static List<String> AirQueryLst=new ArrayList<String>(Arrays.asList("优","良","轻度污染","中度污染","重度污染","严重污染","--"));
	
	///健康指引
	protected static List<String> HealthLst=new ArrayList<String>(Arrays.asList(
			"空气质量令人满意，基本无空气污染",
			"空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响",
			"易感人群症状有轻度加剧，健康人群出现刺激症状",
			"进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响",
			"心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状",
			"健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病",
			"--"));
	
	///建议措施
	protected static List<String> SuggestLst= new ArrayList<String>(Arrays.asList(
			"各类人群可正常活动",
			"极少数异常敏感人群应减少户外活动",
			"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼",
			"儿童、老年人及心脏病、呼吸系统疾病患者应避免长时间、高强度的户外锻炼，一般人群适量减少户外活动",
			"儿童、老年人和心脏病、肺病患者应停留在室内，停止户外运动，一般人群减少户外运动",
			"儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动",
			"--"));
	
	//AQI颜色列表
	protected static List<Integer> ColorLst=new ArrayList<Integer>( Arrays.asList(
				Color.argb(255, 0,228,0),Color.argb(255, 255,255,0),Color.argb(255, 255,126,0),
				Color.argb(255, 255,0,0),Color.argb(255, 153,0,76),Color.argb(255,126,0,35),
				Color.argb(255,169,169,169)));
	
	//AQI资源类型
	public enum AQIPresentEnum
	{
		//数值等级
		Number,
		//罗马数字等级
		RomaNumber,
		//中文数字等级
		ChineseNumber,
		//中文等级
		ChineseLevel,
		//健康指引
		Health,
		//建议措施
		Suggest,
		//颜色
		Color
	}
	
	public enum AQITypedArrayType
	{
		Integer,
		Float,
		String,
		Drawable,
		Color
	}
	
	///把AQI值划分成等级
	public static int GetAirQualityNumberByAQIValue(int aqiValue)
	{
		if(aqiValue<0)return -1;
		if(aqiValue==0)return 1;
		for(int i=0;i<AQIRange.length-1;i++)
			if(aqiValue>AQIRange[i]&&aqiValue<=AQIRange[i+1])
				return i+1;
		return -1;
	}
	
	///通过AQI的数值获取资源
	public static Object GetAQIResourceByAQI(int aqiValue,AQIPresentEnum type)
	{
		int number=GetAirQualityNumberByAQIValue(aqiValue);
		return GetAQIResourceByAQ(number,type);
	}
	
	///通过AQI的字符串数值获取资源
	public static Object GetAQIResourceByAQI(String aqiStrValue,AQIPresentEnum type)
	{
		int aqiValue;
		try
		{
			aqiValue=Integer.parseInt(aqiStrValue);
		}
		catch(Exception ex)
		{
			aqiValue=-1;
		}
		int number= GetAirQualityNumberByAQIValue(aqiValue);
		return GetAQIResourceByAQ(number,type);
		
	}
	
	///通过中文数字等级获取资源
	public static Object GetAQIResourceByChineseLevel(String chlv,AQIPresentEnum type)
	{
		int index=AirQueryLst.indexOf(chlv);
		if(index>-1)index++;
		return GetAQIResourceByAQ(index,type);
	}
	
	///通过数字等级获取资源
	public static Object GetAQIResourceByAQ(int aqValue,AQIPresentEnum type)
	{
		int number=aqValue;
		if(number<1)number=-1;
		else if(number>6)number=-1;
		switch (type) {
		case Number:
			return number;
			//break;
		case RomaNumber:
			return AirQueryRomaLevelLst.get(number==-1?6:number-1);
//			break;
		case ChineseNumber:
			return AirQueryChineseNumberLevelLst.get(number==-1?6:number-1);
//			break;
		case ChineseLevel:
			return AirQueryLst.get(number==-1?6:number-1);
//			break;
		case Health:
			return HealthLst.get(number==-1?6:number-1);
//			break;
		case Suggest:
			return SuggestLst.get(number==-1?6:number-1);
//			break;
		case Color:
			return ColorLst.get(number==-1?6:number-1);
//			break;
		default:
//			break;
			return null;
		}
	}
	
	///通过字符串数字等级获取资源
	public static Object GetAQIResourceByAQ(String aqStrValue,AQIPresentEnum type)
	{
		int aqValue;
		try
		{
			aqValue=Integer.parseInt(aqStrValue);
		}
		catch(Exception ex)
		{
			aqValue=-1;
		}
		return GetAQIResourceByAQ(aqValue,type);
	}
	
	///通过AQI的数值获取自定义资源
	public static Object GetCustomResourceByAQI(int aqiValue,List<Object> resLst,Object defaultValue)
	{
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("资源列表不合法");
		int number= GetAirQualityNumberByAQIValue(aqiValue);
		if(number==-1)return defaultValue;
		else return resLst.get(number-1);
	}
	
	///通过AQI的字符串数值获取自定义资源
	public static Object GetCustomResourceByAQI(String aqiStrValue,List<Object> resLst,Object defaultValue)
	{
		int aqiValue;
		try
		{
			aqiValue=Integer.parseInt(aqiStrValue);
		}
		catch(Exception ex)
		{
			aqiValue=-1;
		}
//		int number= GetAirQualityNumberByAQIValue(aqiValue);
//		return GetCustomResourceByAQI(number, resLst, defaultValue);
		return GetCustomResourceByAQI(aqiValue,resLst,defaultValue);
	}
	
	///通过AQI数字/字符串获取Res数组中的资源
	public static Object GetResourceByAQI(Object aqiValue,int resArrayName,AQITypedArrayType type)
	{
		Class<?> valueClass=aqiValue.getClass();
		int numAQI=-1;
		if(valueClass.equals(Integer.class)||valueClass.getName().equals("int"))
		{
			numAQI=(Integer)aqiValue;
		}
		else if(valueClass.equals(String.class))
		{
			numAQI=Integer.parseInt((String)aqiValue);
		}
		int aq=GetAirQualityNumberByAQIValue(numAQI);
		return GetResourceByAQ(aq,resArrayName,type);
	}
	
	///通过数字/字符等级获取Res数组中的资源
		public static Object GetResourceByAQ(Object aqValue,int resArrayName,AQITypedArrayType type)
		{
			Class<?> valueClass=aqValue.getClass();
			int numAQI=-1;
			if(valueClass.equals(Integer.class)||valueClass.getName().equals("int"))
			{
				numAQI=(Integer)aqValue;
			}
			else if(valueClass.equals(String.class))
			{
				numAQI=Integer.parseInt((String)aqValue);
			}
			int aq=numAQI;//GetAirQualityNumberByAQIValue(numAQI);
			TypedArray ty=SuncereApplication.CurrentApplication().getResources().obtainTypedArray(resArrayName);
			if(aq<1||aq>6)
				aq=6;
			else
				aq--;
			try
			{
				switch (type) {
				case Integer:
					return ty.getInt(aq, -1);
				case Float:
					return ty.getFloat(aq, -1);
				case String:
					return ty.getString(aq);
				case Drawable:
					return ty.getDrawable(aq);
				case Color:
					return ty.getColor(aq, -1);
				default:
					break;
			}
			}finally
			{
				ty.recycle();
			}
			return null;
		}
	
	///通过中文数字等级获取自定义资源
	public static Object GetCustomResourceByChineseLevel(String chlv,List<Object> resLst,Object defaultValue)
	{
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("资源列表不合法");
		int index=AirQueryLst.indexOf(chlv);
		if(index<0||index>=6)return defaultValue;
		else return resLst.get(index);
	}
	
	
	
	///通过数字等级获取自定义资源
	public  static Object GetCustomResourceByAQ(int aqValue,List<Object> resLst,Object defaultValue)
	{
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("资源列表不合法");
		int index=aqValue;
		if(index<0||index>6)return defaultValue;
		else return resLst.get(index-1);
	}
	
	///通过字符串数字等级获取自定义资源
	public static Object GetCustomResourceByAQ(String aqStrValue,List<Object> resLst,Object defaultValue)
	{
		int aqValue;
		try
		{
			aqValue=Integer.parseInt(aqStrValue);
		}
		catch(Exception ex)
		{
			aqValue=-1;
		}
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("资源列表不合法");
		int index=aqValue;
		if(index<0||index>6)return defaultValue;
		else return resLst.get(index-1);
	}
}
