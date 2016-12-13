package suncere.androidapp.attributes;

import java.util.HashMap;
import java.util.List;

public class ListCountAttribute implements IClassAttribute{


	public final static int FROM_HEAD=1;

	public final static int FROM_FOOT=2;
	
	private int pickType;
	private int remainderCount;
	
	public ListCountAttribute(int pickType,int remainderCount)
	{
		this.remainderCount=remainderCount;
		this.pickType=pickType;
	}
	
	public void FiltList( List<? extends Object> datas)
	{
		if(datas==null)return;
		if(pickType==FROM_HEAD)
		{
			while(datas.size()>this.remainderCount)
			{
				datas.remove(datas.size()-1);
			}
		}
		else if(pickType==FROM_FOOT)
		{
			while(datas.size()>this.remainderCount)
			{
				datas.remove(1);
			}
		}
	}
	
}
