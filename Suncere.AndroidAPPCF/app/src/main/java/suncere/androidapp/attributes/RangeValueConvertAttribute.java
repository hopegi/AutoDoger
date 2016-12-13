package suncere.androidapp.attributes;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo;
import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.Convert;

public class RangeValueConvertAttribute extends ConvertableAttribute{

	private List<Object[]> rangeInfoArr;
	private Object defaultValue;
	
	public RangeValueConvertAttribute(String fieldNewName, List<Object[]> rangeInfo) 
	{
		this(fieldNewName, rangeInfo, AttributeUsage.ResultDataHandle);
	}
	
	public RangeValueConvertAttribute(String fieldNewName, List<Object[]> rangeInfo,  AttributeUsage usage)
	{
		this.fieldNewName=fieldNewName;
		this.rangeInfoArr=AnalyzeInfo(  rangeInfo);
		this.usage=usage;
	}

	private List<Object[]> AnalyzeInfo(List<Object[]> rangeInfoLst)
	{
		List<Object[]> result=new ArrayList<Object[]>();
		Object[] rangeInfo;
		String range;
		String [] rangeArr;
		double [] rangeValueArr;
		String [] optionArr;
		for(int i=0;i<rangeInfoLst.size();i++)
		{
			rangeInfo= rangeInfoLst.get(i);
			range= (String) rangeInfo[0];
			if(range==null)
			{
				this.defaultValue=rangeInfo[1];
				continue;
			}
			optionArr=new String[2];
			if(range.contains("["))
			{
				optionArr[0]="[";
				range=range.replace('[', ' ');
			}
			else  if(range.contains("("))
			{
				optionArr[0]="(";
				range=range.replace('(', ' ');
			}
			if(range.contains("]"))
			{
				optionArr[1]="]";
				range=range.replace(']', ' ');
			}
			else  if(range.contains(")"))
			{
				optionArr[1]=")";
				range=range.replace(')', ' ');
			}
			rangeArr= range.trim().split(",",-1);
			rangeValueArr=new double[2];
			rangeArr[0]=rangeArr[0].trim();
			rangeArr[1]=rangeArr[1].trim();
			
			if(rangeArr[0].length()>0)
				rangeValueArr[0]=Double.parseDouble( rangeArr[0]);
			

			if(rangeArr[1].length()>0)
				rangeValueArr[1]=Double.parseDouble( rangeArr[1]);
			
			result.add(new Object[]{ optionArr,rangeValueArr, rangeInfo[1] });
			
		}
		return result;
	}
	
	@Override
	public Object ConvertData(Object value, AutoBaseModel model) {
		
		try {
			
			double numberValue=Double.parseDouble( value.toString());
			Object[] optArr;
			double[] limitArr;
			for(int i=0;i<this.rangeInfoArr.size();i++ )
			{
				Object[] rangeInfo=rangeInfoArr.get(i);
				optArr= (Object[]) rangeInfo[0];
				limitArr=(double[]) rangeInfo[1];
				
				if(optArr[0]!=null)
				{
					if(optArr[0].equals("["))
					{
						if(numberValue<limitArr[0])continue;
					}
					else {
						if(numberValue<=limitArr[0])continue;
					}
				}
				
				if(optArr[1]!=null)
				{
					if(optArr[1].equals("]"))
					{
						if(numberValue>limitArr[1])continue;
					}
					else {
						if(numberValue>=limitArr[1])continue;
					}
				}
				
				return rangeInfo[2];
			}

		} catch (NumberFormatException e) {
//			if(this.defaultValue!=null)
				return defaultValue;
		}
		return null;
	}

}
