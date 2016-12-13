package suncere.androidappcf.aqi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.tools.Convert;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

public class PollutantNameFormatTool {
	
	protected static PollutantNameFormatTool _default=new  PollutantNameFormatTool();
	
	///用于存储各种污染物名称和下标起止索引
	List<List<Object>> pollutantInfoLst;
	
	public static PollutantNameFormatTool GetInstance()
	{
		return _default;
	}
	
	private PollutantNameFormatTool()
	{
		pollutantInfoLst=new ArrayList<List<Object>>();
		pollutantInfoLst.add(new ArrayList(Arrays.asList("SO2",2,3) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("NO2",2,3) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("O3",1,2) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("PM10",2,4) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("PM2.5",2,5) ));
	}
	
	public SpannableString ConvertPollutantAndUnitText(String content,float normalTextSize)
	{
//		SpannableString result=new SpannableString( this.ReplaceUnit( content));
//		List<Integer> so2List=this.FindPollutantIndexs(content, "SO2");
//		List<Integer> no2List=this.FindPollutantIndexs(content, "NO2");
//		List<Integer> o3List=this.FindPollutantIndexs(content, "O3");
//		List<Integer> pm10List=this.FindPollutantIndexs(content, "PM10");
//		List<Integer> pm2_5List=this.FindPollutantIndexs(content, "PM2.5");
//		int smallTextSize=Math.round(normalTextSize/5);
//		for(int i:so2List)
//		{
//			result.setSpan(new AbsoluteSizeSpan(smallTextSize,true), i+2, i+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		for(int i:no2List)
//		{
//			result.setSpan(new AbsoluteSizeSpan(smallTextSize,true), i+2, i+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		for(int i:o3List)
//		{
//			result.setSpan(new AbsoluteSizeSpan(smallTextSize,true), i+1, i+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		for(int i:pm10List)
//		{
//			result.setSpan(new AbsoluteSizeSpan(smallTextSize,true), i+2, i+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		for(int i:pm2_5List)
//		{
//			result.setSpan(new AbsoluteSizeSpan(smallTextSize,true), i+2, i+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		return result;
		
		//更改立方单位
		SpannableString result=new SpannableString( this.ReplaceUnit( content));
		///各种污染物的下标索引字典集
		HashMap<String,List<Integer>> pollutantIndexLst=new HashMap<String,List<Integer>>();
		///遍历污染物下标集合查找并记录各种污染物的下标
		for(List<Object> pollutantInfo : pollutantInfoLst)
		{
			pollutantIndexLst.put(pollutantInfo.get(0).toString(), this.FindPollutantIndexs(content, pollutantInfo.get(0).toString()));
		}
		
		int smallTextSize=Math.round(normalTextSize/5);
		if(smallTextSize==0)smallTextSize=Convert.toInt( normalTextSize);
		List<Integer> indexLst;
		///遍历污染物下标集合，给正文添下标
		for(List<Object> pollutantInfo : pollutantInfoLst)
		{
//			indexLst= pollutantIndexLst.get( pollutantInfoLst.get(0).toString() );//出现空结果 pollutantInfoLst无理由直接get(0)
			indexLst=pollutantIndexLst.get( pollutantInfo.get(0).toString() );
			for(int i:indexLst)
			{
				result.setSpan(new AbsoluteSizeSpan(smallTextSize,true),
						i+Convert.toInt( pollutantInfo.get(1)),
						i+Convert.toInt( pollutantInfo.get(2)),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return result;
	}
	
	protected List<Integer> FindPollutantIndexs(String content,String pollutantCode)
	{
		List<Integer> result=new ArrayList<Integer>();
		int i=0;
		while(i<content.length()&&i!=-1)
		{
			i=content.indexOf(pollutantCode,i);
			if(i!=-1)
				result.add(i++);
		}
		return result;
	}
	
	protected String ReplaceUnit(String content)
	{
		String result=content;
		if(content.contains("/m3"))
			result=content.replace("/m3", "/m³");
		
		return result;
	}
}