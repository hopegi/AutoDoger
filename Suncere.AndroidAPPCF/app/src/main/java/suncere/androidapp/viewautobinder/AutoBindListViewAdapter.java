package suncere.androidapp.viewautobinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import suncere.androidapp.viewautobinder.AutoBindViewPagerAdapter.IViewPageItemViewInitHandler;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.TypeHelper;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class AutoBindListViewAdapter extends SimpleAdapter implements IAutoAdapter{

	List<HashMap<String,Object>> datasource;
	List<HashMap<String,Object>> orgDatasource;
	int layoutId;
	ViewAutoBinder viewAutoBinder;
	Context context;
	
	
	public AutoBindListViewAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.datasource=(List<HashMap<String, Object>>) data;
		this.layoutId=resource;
		this.context=context;
		viewAutoBinder=new ViewAutoBinder(layoutId);
		viewAutoBinder.RegistDataSource("data", data);
	}
	
	public static AutoBindListViewAdapter BuildAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource)
	{
		List<HashMap<String,Object>>  datasource=new ArrayList<HashMap<String,Object>>();
		datasource.addAll((Collection<? extends HashMap<String, Object>>) data);
		AutoBindListViewAdapter result=new AutoBindListViewAdapter(context, datasource, resource);
		result.orgDatasource=(List<HashMap<String, Object>>) data;
		return result;
	}
	
	public void notifyDataSetChanged()
	{
		this.datasource.clear();
		this.datasource.addAll(orgDatasource);
		super.notifyDataSetChanged();
	}
	
	public AutoBindListViewAdapter(Context context,List<? extends Map<String, ?>> data, int resource)
	{
		this(context, data, resource, new String[]{}, new int[]{});
	}
	
	public int getCount()
	{
		return this.datasource.size();
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		View view=super.getView(position, convertView, parent);
		if(TypeHelper.IsSubClassOf(context, IViewPageItemViewInitHandlerV2.class))
		{
			try
			{
				((IViewPageItemViewInitHandlerV2)context).InitView(view,position,datasource.get(position));
			}
			catch(Exception ex)
			{
				
			}
		}
		viewAutoBinder.UpdateContentView(view);
		viewAutoBinder.RegistDataSource("i", position);
		viewAutoBinder.AutoBindData();
		return view;
	}

	@Override
	public ViewAutoBinder GetViewAutoBinder() {
		return this.viewAutoBinder;
	}
	
	public interface IViewPageItemViewInitHandlerV2
	{
		void InitView(View view, int index, HashMap<String, Object> data);
	}
}
