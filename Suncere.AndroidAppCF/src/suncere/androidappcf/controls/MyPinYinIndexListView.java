package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import suncere.androidappcf.R;
import suncere.androidappcf.tools.PinYinHelper;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyPinYinIndexListView extends RelativeLayout {

	final static String[] varStrArray=new String[27];
	List<String> personLst;
	LinearLayout layout;
	TextView tip;
	ListView list;
	int srcHeight=0;
	boolean flag=true;
	private HashMap<String,List< String>> pinYinNameDic;
	private List<String> pinYinLst;
	private List<HashMap<String,Object>> datasource;
	private HashMap<String,Boolean>showDic;
	private HashMap<String, Integer> verIndexDic; 
	int tvHeight;
	int templateViewId;
	MyPinYinIndexListViewEvents events;
	boolean useDefaultLayout;
	
	public final static   String CityNameKey="cityName";
	public final static String CheckKey="Check";
	public final static String PYKey="py";
	public final static String ShowKey="show";

	public MyPinYinIndexListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		IniVarStrArray();
		IniViews(context);
	}

	public MyPinYinIndexListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		IniVarStrArray();
		IniViews(context);
	}

	private void IniViews(Context context)
	{
		LayoutParams layPara;

		list=new ListView(context);
		this.addView(list);
		layPara=(LayoutParams) list.getLayoutParams();
		layPara.width=LayoutParams.MATCH_PARENT;
		layPara.height=LayoutParams.MATCH_PARENT;
		list.setLayoutParams(layPara);
		
		tip=new TextView(context);
		this.addView(tip);
		tip.setVisibility(View.GONE);
		tip.setGravity(Gravity.CENTER);
		tip.setTextSize(30);
		tip.setTextColor(Color.WHITE);
		tip.setBackgroundColor(Color.parseColor("#606060"));
		layPara=(LayoutParams) tip.getLayoutParams();
		layPara.width=180;
		layPara.height=180;
		layPara.addRule(CENTER_IN_PARENT);
		tip.setLayoutParams(layPara);
		
		layout=new LinearLayout(context);
		this.addView(layout);
		layout.setOrientation(1);
		layout.setBackgroundColor(Color.parseColor("#00ffffff"));
		layPara=(LayoutParams) layout.getLayoutParams();
		layPara.width=LayoutParams.WRAP_CONTENT;
		layPara.height=LayoutParams.FILL_PARENT;
		layPara.addRule(ALIGN_PARENT_RIGHT);
		layout.setLayoutParams(layPara);
		
	}


	public void setEvents(MyPinYinIndexListViewEvents events) {
		this.events = events;
	}
	
	public int getTemplateViewId() {
		return templateViewId;
	}

	public void setTemplateViewId(int templateViewId) {
		this.templateViewId = templateViewId;
	}
	
	public void SetDataSource(List<HashMap<String,Object>> datasource)
	{
		this.SetDataSource(datasource,true);
	}
	
	public void SetDataSource(List<HashMap<String,Object>> datasource,boolean UseDefaultLayout)
	{
		this.useDefaultLayout=UseDefaultLayout;
		this.datasource=datasource;
		CreateDataSource();
//		将原始人名转成 拼音+首字母 再排序 得出原始顺序表  （记得对单首字母的人名左特殊处理）
		ConvertPinYin();
//		将人名与拼音建键值对作映射用
		CreatePinYInNameDic();
//		按照原始顺序和键值对生成新最终的列表
		BuildDataSource();
//		根据首字母定出快速跳转索引键值对
		CreateVerIndexDic();
		
		Log.d("datasourceLength", String.valueOf(datasource.size()));
		PinYinCityIndexListViewAdapter adapter=new PinYinCityIndexListViewAdapter(this.getContext(),this.datasource);
		list.setAdapter(adapter);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasChanges)
	{
		if(flag)
		{
			srcHeight=layout.getMeasuredHeight();
			tvHeight=srcHeight/varStrArray.length;
			IniIndexLayout();
			flag=true;
		}
	}

	
	private void CreateDataSource()
	{
		showDic=new HashMap<String,Boolean>();
		personLst=new ArrayList();
		for(HashMap<String,Object> cityItem :datasource ){
			personLst.add(cityItem.get(CityNameKey).toString());
			showDic.put(cityItem.get(CityNameKey).toString(), Boolean.valueOf(cityItem.get(CheckKey).toString()));
		}
	}
	
	private void IniVarStrArray()
	{
		varStrArray[0]="#";
		for(int i=1;i<varStrArray.length;i++)
			varStrArray[i]=String.valueOf((char)(((int)'A')+i-1));
	}

	private void IniIndexLayout()
	{
		for(String str:varStrArray)
		{
			
			final TextView indexTV=new TextView(this.getContext());
			indexTV.setText(str);

			indexTV.setPadding(10, 0, 10, 0);
			layout.addView(indexTV);
			indexTV.getLayoutParams().height=tvHeight;
			indexTV.setLayoutParams(indexTV.getLayoutParams());
		}
			layout.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {

					
					int tvIndex=(int) (event.getY()/tvHeight);
					if(tvIndex>=0&&tvIndex<varStrArray.length){
						String tipText=varStrArray[tvIndex];
						Log.d("TouchIndex", String.valueOf(tipText));
						if(verIndexDic.containsKey(tipText))
						{
							int pos=verIndexDic.get(tipText);
							list.setSelectionFromTop(pos, 0);
							tip.setText(tipText);
							tip.setVisibility(View.VISIBLE);
						}
					}
					if(event.getAction()==MotionEvent.ACTION_DOWN)
					{
						layout.setBackgroundColor(Color.parseColor("#606060"));
						
					}
					else if(event.getAction()==MotionEvent.ACTION_UP)
					{
						layout.setBackgroundColor(Color.parseColor("#00ffffff"));
						tip.setVisibility(View.GONE);
					}
					else if(event.getAction()==MotionEvent.ACTION_MOVE)
					{
						
					}
					
					return true;
					
	
				}
			});
		
	}

	private void ConvertPinYin()
	{
		TreeSet<String> set=new TreeSet<String>(); 
		for(String str : personLst)
			set.add(PinYinHelper.getPinYinHeadChar(str).substring(0, 1));
		
		if(pinYinLst==null)
			pinYinLst=new ArrayList<String>();
		
		pinYinLst.clear();
		
		for(String str :set)
			pinYinLst.add(str);


		
		set.clear();
		for(String name:personLst)
			set.add(PinYinHelper.getPinYinHeadChar(name));
		for(String str :set)
			pinYinLst.add(str);
		
		String[] arrResult=new String[pinYinLst.size()];
		for(int i=0;i<arrResult.length;i++)
			arrResult[i]=pinYinLst.get(i);
		Arrays.sort(arrResult,String.CASE_INSENSITIVE_ORDER);
		pinYinLst.clear();
		pinYinLst=Arrays.asList(arrResult);
	}
	
	private void CreatePinYInNameDic()
	{
		if(pinYinNameDic==null)pinYinNameDic=new HashMap<String,List< String>>();
		pinYinNameDic.clear();
		String pinyin;
		for(String name:personLst)
		{
			pinyin=PinYinHelper.getPinYinHeadChar(name);
			if(!pinYinNameDic.containsKey(pinyin))
				pinYinNameDic.put(pinyin, new ArrayList<String>());
			pinYinNameDic.get(pinyin).add(name);
		}
	}
	
	private void BuildDataSource()
	{
		if(datasource==null)datasource=new ArrayList<HashMap<String,Object>>();
		datasource.clear();
		HashMap<String,Object> temp;
		for(String py : pinYinLst)
		{
			if( py.length()==1){
				temp=new HashMap<String,Object>();
				temp.put(PYKey,py);
				datasource.add(temp);
			}
			else if(pinYinNameDic.containsKey(py))
			{
				for(String name :pinYinNameDic.get(py))
				{
					temp=new HashMap<String,Object>();
					temp.put(PYKey,name);
					temp.put(ShowKey,showDic.get(name));
					datasource.add(temp);
					
				}
			}
		}
	}

	private void CreateVerIndexDic()
	{
		if(verIndexDic==null)
			verIndexDic=new HashMap<String, Integer>();
		verIndexDic.clear();
		for(int i=0;i<datasource.size();i++)
			for(int j=0;j<varStrArray.length;j++)
				if(varStrArray[j].equals( datasource.get(i).get(PYKey)))
					verIndexDic.put(datasource.get(i).get(PYKey).toString(), i);
	}

	public interface MyPinYinIndexListViewEvents
	{
		void BindTemplateItem(int position, View contentView, ViewGroup parent,HashMap<String,Object> datas);
	}
	
	///列表的Adapter
	private class PinYinCityIndexListViewAdapter extends BaseAdapter {

		private List<HashMap<String,Object>> datasource;
		private Context context;
		private BaseAdapter adapter;
		
		public PinYinCityIndexListViewAdapter(Context context,List<HashMap<String,Object>> objects) {
			
			// TODO Auto-generated constructor stub
			this.datasource=objects;
			this.context=context;
			this.adapter=this;
		}
		
	    @Override  
	    public int getCount() {  
	        return this.datasource.size();  
	    }  
	  
	    @Override  
	    public Object getItem(int position) {  
	        return this.datasource.get(position);  
	    }  
	  
	    @Override  
	    public long getItemId(int position) {  
	        return position;  
	    }
		
		@Override
		public boolean isEnabled(int position)
		{
			if(datasource.get(position).get(PYKey).toString().length()==1)
				return false;
			else
				return super.isEnabled(position);
		}
		
		public View getView(  final int position, View convertView, ViewGroup parent)
		{
			TextView tv;
			View result;
			View contentView;
			if(useDefaultLayout)
			{
				result=LayoutInflater.from(context).inflate(R.layout.city_list_item, null);
				if(datasource.get(position).get( PYKey ).toString().length()==1){
					tv=(TextView) result.findViewById(R.id.cliTvIndex);
					contentView=tv;
				}
				else{
					contentView=result.findViewById(R.id.cliCtiyContent);
					tv=(TextView)result.findViewById(R.id.cliTvItem);
					CheckBox cb=(CheckBox)result.findViewById(R.id.cliCbIsShow);
					
					cb.setChecked(Boolean.parseBoolean( datasource.get(position).get( ShowKey  ).toString()));
					
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
						@Override
						public void onCheckedChanged(CompoundButton sender, boolean newValue) {
						
							
//							datasource.get(position).remove(1);
//							datasource.get(position).add(newValue);
							datasource.get(position).remove(ShowKey);
							datasource.get(position).put(ShowKey, newValue);
						}});
				}
				tv.setText(datasource.get(position).get(PYKey).toString());
				contentView.setVisibility(View.VISIBLE);
			}
			else

				result=LayoutInflater.from(context).inflate(templateViewId, null);
			if(events!=null)
				events.BindTemplateItem(position, result, parent, datasource.get(position));
			return result;
		}
	}
}
