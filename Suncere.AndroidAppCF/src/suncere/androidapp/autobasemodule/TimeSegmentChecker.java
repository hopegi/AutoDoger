package suncere.androidapp.autobasemodule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import suncere.androidappcf.tools.DateTimeTool;
import suncere.androidappcf.tools.TypeHelper;

public class TimeSegmentChecker implements IChecker {

	private final static String LogTag="TimeSegmentChecker";
	
	CheckerContext context;
	@Override
	public void SetContext(CheckerContext context) {
		// TODO Auto-generated method stub
		this.context=context;
	}

	@Override
	public boolean ExistData(HashMap<String, Object> parameter) {
		// TODO Auto-generated method stub
		AutoBaseDAL dal=context.getDal();
		AutoBaseModel model=context.getModel();
		List<Object> datas= dal.QueryData(model, parameter);
		if(datas==null|| datas.size()<1)
		{
			Log.d(LogTag, "TimeSegmentChecker "+model.getClass().getName()+" 数据库中无数据 直接返回False");
			return false;
		}
		AutoBaseModel dataFirst= (AutoBaseModel) datas.get(0);
		Method timepointGetter= TypeHelper.Getter(model.getClass(), "TimePoint");
		Method timeRangeValueGetter=TypeHelper.Getter(model.getClass(), "TimeRangeValue");
		Method timeRangeGetter=TypeHelper.Getter(model.getClass(), "TimeRange");
		try {
			String timeStr=(String) timepointGetter.invoke(dataFirst, null);
			String timeRangeValue=(String) timeRangeValueGetter.invoke(dataFirst, null);
			int timeRange=  (Integer) timeRangeGetter.invoke(dataFirst, null);
			
			
			if(timeStr==null||timeStr.length()==0)
			{

				Log.d(LogTag, "TimeSegmentChecker "+model.getClass().getName()+" 时间字段没有值 直接返回False");
				return false;
			}
				
			Date date=DateTimeTool.Prase(timeStr);
			String[] timeRangeValueArr=timeRangeValue.split(",");
			int year=Integer.parseInt(timeRangeValueArr[0]);
			int timeRangeIndex=Integer.parseInt(timeRangeValueArr[1]);
			
			int dataYear=DateTimeTool.GetYear(date);
			int dataTimeRangeIndex=-1;
			if(dataYear>year)
			{
				Log.d(LogTag, String.format( "TimeSegmentChecker %s year %d>%d 直接返回True",model.getClass().getName(),dataYear,year));
				return true;
			}
			switch(timeRange)
			{
			case 1:
				dataTimeRangeIndex=DateTimeTool.GetWeekOfYear(date);
				break;
			case 2:
				dataTimeRangeIndex=DateTimeTool.GetMonth(date);
				break;
			case 3:
				dataTimeRangeIndex=DateTimeTool.GetSeason(date);
				break;
			case 4:
				Log.d(LogTag, String.format( "TimeSegmentChecker %s year %d~%d %b",model.getClass().getName(),dataYear,year,dataYear>year));
				return dataYear>year;
			}
			
			Log.d(LogTag, String.format( "TimeSegmentChecker %s TimeRangeIndex %d~%d %b",
					model.getClass().getName(),
					dataTimeRangeIndex,
					timeRangeIndex, 
					dataTimeRangeIndex>timeRangeIndex));
			
			return dataTimeRangeIndex>timeRangeIndex;
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(LogTag, "TimeSegmentChecker throw a exception 直接返回False");
		return false;
	}

}
