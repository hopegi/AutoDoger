package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.tools.Convert;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.SubscriptSpan;
import android.util.AttributeSet;
import android.widget.TextView;


public class PollutantNameTextView extends TextView {

	

	///用于存储各种污染物名称和下标起止索引
	List<List<Object>> pollutantInfoLst;
	
	public PollutantNameTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Inis();
	}

	public PollutantNameTextView(Context context) {
		super(context);
		Inis();
	}
	
	public PollutantNameTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		Inis();
	}
	
	private void Inis()
	{
		pollutantInfoLst=new ArrayList<List<Object>>();
		pollutantInfoLst.add(new ArrayList(Arrays.asList("SO2",2,3) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("NO2",2,3) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("O3",1,2) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("PM10",2,4) ));
		pollutantInfoLst.add(new ArrayList(Arrays.asList("PM2.5",2,5) ));
		if(this.getText()!=null)
			this.setText(this.ConvertPollutantAndUnitText(this.getText().toString()));
		
	}
	
	public void setText(String text)
	{
		super.setText( this.ConvertPollutantAndUnitText(text));
	}
	
	public SpannableString ConvertPollutantAndUnitText(String content)
	{
		//更改立方单位
		SpannableString result=new SpannableString( this.ReplaceUnit( content));
		///各种污染物的下标索引字典集
		HashMap<String,List<Integer>> pollutantIndexLst=new HashMap<String,List<Integer>>();
		///遍历污染物下标集合查找并记录各种污染物的下标
		for(List<Object> pollutantInfo : pollutantInfoLst)
		{
			pollutantIndexLst.put(pollutantInfo.get(0).toString(), this.FindPollutantIndexs(content, pollutantInfo.get(0).toString()));
		}
		
		int smallTextSize=Math.round(this.getTextSize()/5);
		if(smallTextSize<10&&this.getTextSize()>10)smallTextSize=10;
		else if(smallTextSize<10&&this.getTextSize()<=10)smallTextSize= (int) (this.getTextSize()>1?this.getTextSize()-1:this.getTextSize());
		List<Integer> indexLst;
		///遍历污染物下标集合，给正文添下标
		for(List<Object> pollutantInfo : pollutantInfoLst)
		{
			indexLst= pollutantIndexLst.get( pollutantInfo.get(0).toString() );
			if(indexLst==null)continue;
			for(int i:indexLst)
			{
				result.setSpan(new AbsoluteSizeSpan(smallTextSize,true),
						i+Convert.toInt( pollutantInfo.get(1)),
						i+Convert.toInt( pollutantInfo.get(2)),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				result.setSpan(new SubscriptSpan(),
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
			result=content.replace("/m3", "/m³");//+this.getContext().getResources().getString(R.string.upThree));
		
		return result;
	}
	 @Override  
	    public boolean isFocused() {  
	        return true;  
	  
	    } 

}