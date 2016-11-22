package suncere.androidapp.viewautobinder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import suncere.androidappcf.R;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.DateTimeTool;
import suncere.androidappcf.tools.DictionaryHelper;
import suncere.androidappcf.tools.StopWatch;
import suncere.androidappcf.tools.TypeHelper;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ViewAutoBinder {

	//存放各个视图的绑定信息
	private HashMap<Integer,HashMap<String,Object>> bindingInfo;
	private HashMap<String,String> defineCollection;
	private HashMap<String,Object> datasourceTypeDic;
	
	private List<View> unRefreshViews;

	//数据源集合
	private HashMap<String,Object> datasourceCollection;
	
	private Activity activity;
	
	private View contentView;
	
	private boolean isActivityMode;

	public ViewAutoBinder()
	{
		bindingInfo=new  HashMap<Integer,HashMap<String,Object>>();
		datasourceCollection=new HashMap<String,Object>();
		defineCollection=new HashMap<String,String>();
		datasourceTypeDic=new HashMap<String,Object>();
	}
	
	public ViewAutoBinder(Activity activity)
	{
		this();
		this.activity=activity;
		this.isActivityMode=true;
	}
	
	public ViewAutoBinder(View contentView)
	{
		this();
		this.contentView=contentView;
		this.isActivityMode=false;
	}
	
	public ViewAutoBinder(int layoutId)
	{
		this();
		this.AnalyzeViewBindingInfo(layoutId);
	}
	
	public HashMap<String, String> getDefineCollection() {
		return defineCollection;
	}

	public void setDefineCollection(HashMap<String, String> defineCollection) {
		this.defineCollection = defineCollection;
	}

	public void setUnRefreshViews(List<View> unRefreshViews) {
		this.unRefreshViews = unRefreshViews;
	}
	
	public void UpdateContentView(View contentView)
	{
		this.contentView=contentView;
		this.isActivityMode=false;
	}
	
	private View findViewById(int id)
	{
		if(this.isActivityMode)
		{
			return this.activity.findViewById(id);
		}
		else
		{
			return this.contentView.findViewById(id);
		}
	}
	
	private Context GetContext()
	{
		if(this.isActivityMode)
			return this.activity;
		else if(this.contentView!=null)
			return this.contentView.getContext();
		return SuncereApplication.CurrentApplication();
	}
	
	public void AnalyzeViewBindingInfo(AttributeSet attrs)
	{
		TypedArray ta=GetContext().obtainStyledAttributes(attrs,R.styleable.View);
		
//		bindingInfo
		HashMap<String,Object> bindingConfig=new HashMap<String,Object>();
		if(ta.getIndexCount()>0)
		{
			//对各个Series进行扫描
//			String tmp=null;
//			
//			tmp=ta.getString( R.styleable.View_adapter );
//			if(tmp!=null&&tmp.length()>0)
//				bindingConfig.put("adapter", tmp);
			
			//只有attrset,/无法预知view种类,只能获取所有
			List<IViewBinder> binderLst=ViewBinderProvider.Current().GetAllViewBinder();
			for(IViewBinder binder : binderLst)
			{
				bindingConfig.putAll(binder.GetBindingConfig(ta));
			}
		}
		//特别要提取id值
		if(bindingConfig.size()>0)
		{
			int viewId=-1;
			for(int i=0;i<attrs.getAttributeCount();i++)
			{
				if(attrs.getAttributeName(i).equals("id"))
				{
					viewId=attrs.getAttributeResourceValue(i, -1);
					if(viewId==-1)
						Log.d("", "不存在id ");
					else if( bindingInfo.containsKey(viewId))
					{
						Log.d("", "重复id");
						bindingInfo.remove(viewId);
						bindingInfo.put(viewId, bindingConfig);
					}
					else
						bindingInfo.put(viewId, bindingConfig);
				}
			}
		}
		
		ta.recycle(); 
	}
	
	public void AnalyzeViewBindingInfo(int layoutId)
	{
		XmlResourceParser parser=this.GetContext() .getResources().getLayout(layoutId);
		
		String xmlStr="";
		int tmpId;
		//View preView=new View(this);
		try {
			int eventType=parser.getEventType();
			while(eventType!=  XmlPullParser.END_DOCUMENT )
			{
				if(eventType == XmlPullParser.START_TAG) {
		        	 
					if(!parser.getName().equals("include"))
						this.AnalyzeViewBindingInfo(parser);
					else
					{
						layoutId=  parser.getAttributeResourceValue(null, "layout", -1);
						AnalyzeViewBindingInfo(layoutId);
					}
		        	  
		          }
				eventType= parser.next();
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void RegistDataSource(String name,Object datasource)
	{
//		if(this.datasourceCollection.containsKey(name))
//		{
//			this.datasourceCollection.remove(name);
//		}
//		this.datasourceCollection.put(name, datasource);
		this.RegistDataSource(name, datasource,DatasourceTypeEnum.Multi);
	}
	
	public void RegistDataSource(String name,Object datasource,DatasourceTypeEnum dsType)
	{
		DictionaryHelper.OverrideAdd(datasourceCollection, name, datasource);
		DictionaryHelper.OverrideAdd(datasourceTypeDic, name, dsType);
	}
	
	
	public void AutoBindData()
	{
		View view;
		List<IViewBinder> binderLst;
		
		for(Entry<Integer,HashMap<String,Object>> item : bindingInfo.entrySet())
		{
			view=this.findViewById(item.getKey());
			
			if(this.unRefreshViews!=null&&this.unRefreshViews.contains(view))continue;
			
			HashMap<String,Object> bindingValue=new HashMap<String,Object>();
			BaseBinderComplier complier=BinderComplierFactory.GetComplier();
			HashMap<String,Object> bindingConfig=item.getValue();
			//设置数据源
			HashMap<String,Object> bindingDatasource=new HashMap<String,Object>();
			for(Entry<String,Object>  kvp:datasourceCollection.entrySet())
			{
				if(datasourceTypeDic.get(kvp.getKey()).equals( DatasourceTypeEnum.Single ))
				{
					if (kvp.getValue().getClass().isArray())
					{
						bindingDatasource.put(kvp.getKey(),  Array.get(kvp.getValue(), 0) );
					}
					else
					{
						List<? extends Object> ds=((List<? extends Object>)kvp.getValue());
						if(ds!=null&&ds.size()>0)
							bindingDatasource.put(kvp.getKey(),ds.get(0));
					}
				}
				else
					bindingDatasource.put(kvp.getKey(), kvp.getValue());
			}
			complier.InitComplier(bindingDatasource);
			//设置预处理参数
			if(complier instanceof DefaultBinderComplier )
			{
				((DefaultBinderComplier)complier).SetPretreatmentParameter(defineCollection);
			}
				
			///编译表达式

			for(Entry<String,Object> kvp: bindingConfig.entrySet())
			{
				try
				{
					bindingValue.put(kvp.getKey(), complier.ComplieValue(kvp.getValue()));
				}catch(Exception ex)
				{
					Log.d("", "suncere.androidapp.viewautobinder.ViewAutoBinder  "+ kvp.getKey() +" compling Value  "+kvp.getValue() +" "+ex);
				}
			}

			binderLst=ViewBinderProvider.Current().GetViewBinder(view);
			
			//给绑定数据
			for(IViewBinder binder: binderLst)
			{
				try
				{
					if(TypeHelper.IsSubClassOf(binder, IChildViewAutoBinder.class))
						((IChildViewAutoBinder)binder).PassParentViewAutoBinder(this);
					binder.BindViewValue(bindingValue, view);
				}catch(Exception ex)
				{
					Log.d("", "suncere.androidapp.viewautobinder.ViewAutoBinder  "+ view +" binding Value  "+binder +" "+ex);
				}
				
			}
		}
		
		if(this.unRefreshViews!=null)
		{
			this.unRefreshViews.clear();
			this.unRefreshViews=null;
		}
		
	}
}
