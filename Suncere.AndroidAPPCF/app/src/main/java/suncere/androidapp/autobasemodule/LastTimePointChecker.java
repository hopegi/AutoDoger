package suncere.androidapp.autobasemodule;

import java.util.Date;
import java.util.HashMap;

import android.util.Log;
import suncere.androidappcf.tools.DateTimeTool;

public class LastTimePointChecker implements IChecker{

//	public final static String DAL="DAL";
//	public final static String MODEL="Model";
	private final static String LogTag="LastTimePointChecker";
	
	
	///小时偏移
	private int hourOffest;
	///分钟偏移
	private int minuteOffset;
	///相差分钟值
	private int valiableMinutes;
	
	private CheckerContext context;
	
	public LastTimePointChecker()
	{
		this.hourOffest=-1;
		this.minuteOffset=-1;
		this.valiableMinutes=-1;
	}
	
	public LastTimePointChecker(int hourOffest,int minuteOffset,int valiableMinutes)
	{
		this();
		if(hourOffest!=-1)
			this.hourOffest=hourOffest;
		if(minuteOffset!=-1)
			this.minuteOffset=minuteOffset;
		if(valiableMinutes!=-1)
			this.valiableMinutes=valiableMinutes;
	}
	
	@Override
	public boolean ExistData(HashMap<String, Object> parameter) {
		
		boolean result=false;
		
		AutoBaseDAL dal=context.getDal();
		AutoBaseModel model=context.getModel();
		String strTimePoint=dal.GetLastTime(model);
		if(strTimePoint==null||strTimePoint.length()==0)
			return result;
		Date oldDate=DateTimeTool.Prase(strTimePoint);
		Date newDate=new Date();

		if(newDate.getMinutes()<this.minuteOffset)
			newDate.setHours(newDate.getHours()-1);
		if(this.hourOffest>-1)
			newDate.setHours(this.hourOffest);
		newDate.setMinutes(0);
		float totalValue=DateTimeTool.TotalMinutes(oldDate, newDate);
		if(this.valiableMinutes>-1)
			result= totalValue<this.valiableMinutes&&totalValue>0  ;
		else
			result=totalValue>0;
			
		Log.d(LogTag, String.format("LastTimePointChecker  %s %s~%s = %f   %b", model.getClass().getName(),strTimePoint,   DateTimeTool.ToString(newDate, "yyyy-MM-dd HH:mm:ss"),totalValue,result ));
			
		return result;
	}

	@Override
	public void SetContext(CheckerContext context) {
		this.context=context;
	}

}
