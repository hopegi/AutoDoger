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
	
	/// 1,2,3,4,5,6 ���ȼ�
	protected static List<Integer> AirQueryNumberLst=new ArrayList<Integer>( Arrays.asList(1,2,3,4,5,6));
	
	///�������ֵȼ�
	protected static List<String> AirQueryRomaLevelLst=new ArrayList<String>(Arrays.asList("��","��","��","����","����","����","--"));
	
	//�������ֵȼ�
	protected static List<String> AirQueryChineseNumberLevelLst=new ArrayList<String>(Arrays.asList("һ��","����","����","�ļ�","�弶","����","--"));
	
	///�ȼ�
	protected static List<String> AirQueryLst=new ArrayList<String>(Arrays.asList("��","��","�����Ⱦ","�ж���Ⱦ","�ض���Ⱦ","������Ⱦ","--"));
	
	///����ָ��
	protected static List<String> HealthLst=new ArrayList<String>(Arrays.asList(
			"���������������⣬�����޿�����Ⱦ",
			"���������ɽ��ܣ���ĳЩ��Ⱦ����ܶԼ������쳣������Ⱥ�����н���Ӱ��",
			"�׸���Ⱥ֢״����ȼӾ磬������Ⱥ���ִ̼�֢״",
			"��һ���Ӿ��׸���Ⱥ֢״�����ܶԽ�����Ⱥ���ࡢ����ϵͳ��Ӱ��",
			"���ಡ�ͷβ�����֢״�����Ӿ磬�˶����������ͣ�������Ⱥ�ձ����֢״",
			"������Ⱥ�˶����������ͣ�������ǿ��֢״����ǰ����ĳЩ����",
			"--"));
	
	///�����ʩ
	protected static List<String> SuggestLst= new ArrayList<String>(Arrays.asList(
			"������Ⱥ�������",
			"�������쳣������ȺӦ���ٻ���",
			"��ͯ�������˼����ಡ������ϵͳ��������Ӧ���ٳ�ʱ�䡢��ǿ�ȵĻ������",
			"��ͯ�������˼����ಡ������ϵͳ��������Ӧ���ⳤʱ�䡢��ǿ�ȵĻ��������һ����Ⱥ�������ٻ���",
			"��ͯ�������˺����ಡ���β�����Ӧͣ�������ڣ�ֹͣ�����˶���һ����Ⱥ���ٻ����˶�",
			"��ͯ�������˺Ͳ���Ӧ���������ڣ������������ģ�һ����ȺӦ���⻧��",
			"--"));
	
	//AQI��ɫ�б�
	protected static List<Integer> ColorLst=new ArrayList<Integer>( Arrays.asList(
				Color.argb(255, 0,228,0),Color.argb(255, 255,255,0),Color.argb(255, 255,126,0),
				Color.argb(255, 255,0,0),Color.argb(255, 153,0,76),Color.argb(255,126,0,35),
				Color.argb(255,169,169,169)));
	
	//AQI��Դ����
	public enum AQIPresentEnum
	{
		//��ֵ�ȼ�
		Number,
		//�������ֵȼ�
		RomaNumber,
		//�������ֵȼ�
		ChineseNumber,
		//���ĵȼ�
		ChineseLevel,
		//����ָ��
		Health,
		//�����ʩ
		Suggest,
		//��ɫ
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
	
	///��AQIֵ���ֳɵȼ�
	public static int GetAirQualityNumberByAQIValue(int aqiValue)
	{
		if(aqiValue<0)return -1;
		if(aqiValue==0)return 1;
		for(int i=0;i<AQIRange.length-1;i++)
			if(aqiValue>AQIRange[i]&&aqiValue<=AQIRange[i+1])
				return i+1;
		return -1;
	}
	
	///ͨ��AQI����ֵ��ȡ��Դ
	public static Object GetAQIResourceByAQI(int aqiValue,AQIPresentEnum type)
	{
		int number=GetAirQualityNumberByAQIValue(aqiValue);
		return GetAQIResourceByAQ(number,type);
	}
	
	///ͨ��AQI���ַ�����ֵ��ȡ��Դ
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
	
	///ͨ���������ֵȼ���ȡ��Դ
	public static Object GetAQIResourceByChineseLevel(String chlv,AQIPresentEnum type)
	{
		int index=AirQueryLst.indexOf(chlv);
		if(index>-1)index++;
		return GetAQIResourceByAQ(index,type);
	}
	
	///ͨ�����ֵȼ���ȡ��Դ
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
	
	///ͨ���ַ������ֵȼ���ȡ��Դ
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
	
	///ͨ��AQI����ֵ��ȡ�Զ�����Դ
	public static Object GetCustomResourceByAQI(int aqiValue,List<Object> resLst,Object defaultValue)
	{
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("��Դ�б��Ϸ�");
		int number= GetAirQualityNumberByAQIValue(aqiValue);
		if(number==-1)return defaultValue;
		else return resLst.get(number-1);
	}
	
	///ͨ��AQI���ַ�����ֵ��ȡ�Զ�����Դ
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
	
	///ͨ��AQI����/�ַ�����ȡRes�����е���Դ
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
	
	///ͨ������/�ַ��ȼ���ȡRes�����е���Դ
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
	
	///ͨ���������ֵȼ���ȡ�Զ�����Դ
	public static Object GetCustomResourceByChineseLevel(String chlv,List<Object> resLst,Object defaultValue)
	{
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("��Դ�б��Ϸ�");
		int index=AirQueryLst.indexOf(chlv);
		if(index<0||index>=6)return defaultValue;
		else return resLst.get(index);
	}
	
	
	
	///ͨ�����ֵȼ���ȡ�Զ�����Դ
	public  static Object GetCustomResourceByAQ(int aqValue,List<Object> resLst,Object defaultValue)
	{
		if(resLst==null||resLst.size()<6)
			throw new IllegalArgumentException("��Դ�б��Ϸ�");
		int index=aqValue;
		if(index<0||index>6)return defaultValue;
		else return resLst.get(index-1);
	}
	
	///ͨ���ַ������ֵȼ���ȡ�Զ�����Դ
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
			throw new IllegalArgumentException("��Դ�б��Ϸ�");
		int index=aqValue;
		if(index<0||index>6)return defaultValue;
		else return resLst.get(index-1);
	}
}
