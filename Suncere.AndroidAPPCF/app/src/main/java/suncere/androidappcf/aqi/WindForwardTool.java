package suncere.androidappcf.aqi;

import java.lang.reflect.Array;
import java.util.List;

public class WindForwardTool {

	///角度范围
	protected static Double[] AngleRange=    { 11.25,33.75,56.25,78.75,101.25,123.75,146.25,168.75,191.25,213.75,236.25,258.75,281.25,303.75,326.25,348.75 };
	
	///方位
	protected static String[] ChineseForwardName={"北","北北东","北东","东北东","东","东南东","南东","南南东","南","南南西","南西","西南西","西","西北西","北西","北北西"};
	
	///记录符号
	protected static String[] MarkForwardName={"N","NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};
	
	///中心角度
	protected static Double[] CentralAngle={0.0,22.5,45.0,67.5,90.0,112.5,135.0,157.5,180.0,202.5,225.0,247.5,270.0,292.5,315.0,337.5};
	
	public enum WindForwardPresentEnum
	{
		AngleRange,
		ChineseForward,
		MarkForward,
		CentralAngle,
	}
	
	protected static int GetResourceIndex(float angle)
	{
		int result=0;
		for(int i=0;i<AngleRange.length;i++)
		{
			if(angle<=AngleRange[i])
			{
				result=i;
				break;
			}
		}
		return result;
	}

	protected static Object GetResource(int index,WindForwardPresentEnum presentType)
	{
		Object result=null;
		
		switch (presentType) {
		case AngleRange:
			int preIndex=index==0?AngleRange.length-1:index;
			Double[] datas= { AngleRange[ preIndex ]  ,    AngleRange[ index] };
			result=datas;
			break;

		case ChineseForward:
			result=ChineseForwardName[index];
			break;
		case MarkForward:
			result=MarkForwardName[index];
			break;
		case CentralAngle:
			result=CentralAngle[index];
			break;	
		}
		
		return result;
	}

	public static Object GetResourceByAngle(float angle,WindForwardPresentEnum presentType)
	{
		Object result=null;
		
		int index=GetResourceIndex(angle);
		result=GetResource(index,presentType);
		
		return result;
	}

	public static Object GetResourceByAngle(String angle,WindForwardPresentEnum presentType)
	{
		float fValue=Float.parseFloat(angle);
		return GetResourceByAngle(fValue,presentType);
	}
	
	public static Object GetResourceByAngle(Object angle,WindForwardPresentEnum presentType)
	{
		float fValue=Float.parseFloat(angle.toString());
		return GetResourceByAngle(fValue,presentType);
	}
	
	public static Object GetCustomResourceByAngle(float angle,List<Object> resLst,Object defaultValue)
	{
		Object result=null;
		
		int index=GetResourceIndex( angle);
		if(resLst==null||resLst.size()<AngleRange.length)
			throw new IllegalArgumentException("资源列表不合法");
		if(index>=resLst.size())
			result=defaultValue;
		result=resLst.get(index);
		
		return result;
	}

	public static Object GetCustomResourceByAngle(String angle,List<Object> resLst,Object defaultValue)
	{
		float fValue=Float.parseFloat(angle);
		return GetCustomResourceByAngle(fValue,resLst,defaultValue);
	}
	
	public static Object GetCustomResourceByAngle(Object angle,List<Object> resLst,Object defaultValue)
	{
		float fValue=Float.parseFloat(angle.toString());
		return GetCustomResourceByAngle(fValue,resLst,defaultValue);
	}
	
}
