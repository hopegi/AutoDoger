package suncere.androidapp.viewautobinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import suncere.androidappcf.tools.TypeHelper;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AutoBindViewPagerAdapter extends PagerAdapter  implements IAutoAdapter {

	Context context;
	List<HashMap<String,Object>> datasource,orgDatasource;
	int layoutId;
	ViewAutoBinder viewAutoBinder;
	List<View> viewLst;
	
	public AutoBindViewPagerAdapter(Context context,List<? extends Map<String, ?>> data,int resource)
	{
		this.context=context;
		this.orgDatasource=(List<HashMap<String, Object>>) data;
		datasource=new ArrayList<HashMap<String,Object>>();
		datasource.addAll(orgDatasource);
		this.layoutId=resource;
		
		viewLst=new ArrayList<View>();
		viewAutoBinder=new ViewAutoBinder(layoutId);
		viewAutoBinder.RegistDataSource("data", datasource);
	}
	
	@Override
	public int getCount() {
		return datasource.size();
	}

	///防止notifyDataSetChanged无反应
	@Override    
	public int getItemPosition(Object object) {    
	    return POSITION_NONE;    
	}  
	
	@Override
	public boolean isViewFromObject(View view1, Object view2) {
		return view1==view2;
	}

	@Override
	public ViewAutoBinder GetViewAutoBinder() {
		return this.viewAutoBinder;
	}

	
//	List<View> viewLst;
//	public MyViewPagerAdapter(List<View> viewLst)
//	{
//		this.viewLst=viewLst;
//	}


	public Object instantiateItem(ViewGroup container,int position)
	{
		while(viewLst.size()<=position)
		{
			View v=((LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
			if(TypeHelper.IsSubClassOf(context, IViewPageItemViewInitHandler.class))
			{
				try
				{
					((IViewPageItemViewInitHandler)context).InitView(v);
				}
				catch(Exception ex)
				{
					
				}
			}
			this.viewLst.add(v);
		}
		container.addView(viewLst.get(position), 0);
		View itemView= viewLst.get(position);
		viewAutoBinder.UpdateContentView(itemView);
		viewAutoBinder.RegistDataSource("i", position);
		viewAutoBinder.AutoBindData();
		return itemView;
	}

	public void notifyDataSetChanged()
	{
		this.datasource.clear();
		this.datasource.addAll(orgDatasource);
		super.notifyDataSetChanged();
	}
	
	public void destoryItem(ViewGroup container,int postion,Object object)
	{
		container.removeView(viewLst.get(postion));
		while(viewLst.size()>this.datasource.size())
		{
			viewLst.remove(viewLst.size()-1);
		}
	}
	
	public void destroyItem(View container,int position,Object object)
	{
		((ViewGroup)container).removeView(viewLst.get(position));
		while(viewLst.size()>this.datasource.size())
		{
			viewLst.remove(viewLst.size()-1);
		}
	}
	
	public interface IViewPageItemViewInitHandler
	{
		void InitView(View view);
	}
}
