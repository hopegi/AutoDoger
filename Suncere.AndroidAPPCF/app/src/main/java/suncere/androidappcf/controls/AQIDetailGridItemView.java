package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import suncere.androidappcf.R;
import suncere.androidappcf.aqi.AQITool;
import suncere.androidappcf.aqi.PollutantNameFormatTool;
import suncere.androidappcf.tools.Convert;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AQIDetailGridItemView extends LinearLayout {

    protected List<HashMap<String,Object>> dataSource;
    protected HashMap<String,HashMap<String,Object>> dicDataItem;
    protected AQIDetailGridViewAdapter sa;
    protected View contentView;

    public AQIDetailGridItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        IniDataSource();
        contentView= LinearLayout.inflate(context,  R.layout.aqi_detail_view,this);
        SetGridViewContent();
    }

    public AQIDetailGridItemView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        IniDataSource();
        contentView=LinearLayout.inflate(context, R.layout.aqi_detail_view, this);
        SetGridViewContent();
    }

    public void SetTitle(String title)
    {
    	((TextView)this.findViewById(R.id.advLbViewTitle)).setText(title);
    }
    
    protected void AddDataItemToView(String pollutant,int value,int state,String unit)
    {
    	HashMap<String,Object> item;
    	 item=new HashMap<String,Object>();
         item.put("pollutant", pollutant);
         item.put("value", value);
         item.put("state", state);
         item.put("unit",  unit);
         dataSource.add(item);
         dicDataItem.put(item.get("pollutant").toString(), item);
    }
    
    protected void AddDataItemToView(String pollutant,String unit)
    {
    	this.AddDataItemToView(pollutant, 0, 1, unit);
    }
    
    protected void IniDataSource()
    {
        dataSource=new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> item;
        dicDataItem=new HashMap<String,HashMap<String,Object>>();
        
        String mgpm3=getResources().getString(R.string.UnitMilligram);
        String ugpm3= getResources().getString(R.string.UnitMicrogram);
        this.AddDataItemToView("SO2",ugpm3 );
        this.AddDataItemToView("CO",mgpm3 );
        this.AddDataItemToView("NO2",ugpm3 );
        this.AddDataItemToView("O3",ugpm3 );
        this.AddDataItemToView("PM10",ugpm3 );
        this.AddDataItemToView("PM2.5",ugpm3 );

    }

    private void SetGridViewContent()
    {
        GridView gv=(GridView) this.findViewById(R.id.advGvDetail);
        sa=new AQIDetailGridViewAdapter(this.getContext(),dataSource,
                R.layout.aqi_detail_view_grid_item,
                new String[]{"pollutant","value","unit"},
                new int[]{R.id.advgiPollutantName,R.id.advgiValue,R.id.advgiUnit});
        gv.setAdapter(sa);
    }

    ///设置AQI值
    public void setDataSource(HashMap<String,Object> datas)
    {
        dicDataItem.get("SO2").put("value",0);
        dicDataItem.get("CO").put("value",0);
        dicDataItem.get("NO2").put("value",0);
        dicDataItem.get("O3").put("value",0);
        dicDataItem.get("PM10").put("value",0);
        dicDataItem.get("PM2.5").put("value",0);

        Iterator<String>  iterator=datas.keySet().iterator();
        String key;
        while(iterator.hasNext())
        {
            key=iterator.next().toString();
            if(dicDataItem.containsKey(key))
                dicDataItem.get(key).put("value", datas.get(key));
        }
        sa.notifyDataSetChanged();
    }

    ///设置IAQI值
    public void setIAQIState(HashMap<String,Object> datas)
    {
        dicDataItem.get("SO2").put("state",-99);
        dicDataItem.get("CO").put("state",-99);
        dicDataItem.get("NO2").put("state",-99);
        dicDataItem.get("O3").put("state",-99);
        dicDataItem.get("PM10").put("state",-99);
        dicDataItem.get("PM2.5").put("state",-99);

        Iterator<String>  iterator=datas.keySet().iterator();
        String key;
        while(iterator.hasNext())
        {
            key=iterator.next().toString();
            if(dicDataItem.containsKey(key))
                dicDataItem.get(key).put("state", datas.get(key));
        }
        sa.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        return false;
    }
    
     private class AQIDetailGridViewAdapter extends SimpleAdapter {

    	List< HashMap<String,Object>> dataSource;
    	public AQIDetailGridViewAdapter(Context context,
    			List<? extends Map<String, ?>> data, int resource, String[] from,
    			int[] to) {
    		super(context, data, resource, from, to);
    		// TODO Auto-generated constructor stub
    		this.dataSource=(List<HashMap<String, Object>>) data;
    	}

    	public View getView(int position,View convertView,ViewGroup parent)
    	{
    		View view=super.getView(position, convertView, parent);
    		
    		ImageView iv=(ImageView)view.findViewById(R.id.advgiImgGrade);
    		
    		iv.setBackgroundColor( Convert.toInt(  AQITool.GetAQIResourceByAQ( Convert.toInt

(dataSource.get(position).get("state")) , AQITool.AQIPresentEnum.Color)));
    		
    		TextView tv=(TextView)view.findViewById(R.id.advgiPollutantName);
    		tv.setText( PollutantNameFormatTool.GetInstance().ConvertPollutantAndUnitText(tv.getText

().toString(), tv.getTextSize()));
    
    		
    		return view;
    	}
    }
}
