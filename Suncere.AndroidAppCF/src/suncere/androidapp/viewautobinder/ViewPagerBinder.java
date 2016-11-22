package suncere.androidapp.viewautobinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewPagerBinder implements IViewBinder,IChildViewAutoBinder {

	private String[] supportAttrsNameArr=new String[]{ "layout","datasource","adapter" };
	private ViewAutoBinder parentViewAutoBinder;
	
	@Override
	public List<String> SupportAttrsName() {
		return Arrays.asList( supportAttrsNameArr);
	}

	@Override
	public Class<?> SupportViewType() {
		return ViewPager.class;
	}

	@Override
	public void BindViewValue(HashMap<String, Object> values, View view) {
		if(!(view instanceof ViewPager))
		{
			Log.d("", " 给定View不是ViewPager 及其子类  ");
			return;
		}
		ViewPager viewPager=(ViewPager)view;
		PagerAdapter adapter=(PagerAdapter) viewPager.getAdapter();
		List<HashMap<String,Object>> ds=null;
		if(values.containsKey("datasource"))
		{
			ds=(List<HashMap<String, Object>>) values.get("datasource");
		}
		else
		{
			Log.d("", "ListViewBinder 缺少......datasource");
		}
		if(adapter==null)
		{
			if(values.containsKey("adapter"))
			{
				adapter=(AutoBindViewPagerAdapter) values.get("adapter");
				viewPager.setAdapter(adapter);
			}
			else
			{
				int layoutId=-1;

				if(values.containsKey("layout"))
				{
					layoutId=Integer.parseInt( values.get("layout").toString());
				}
				else
				{
					Log.d("", "ListViewBinder 缺少......layout");
				}
				AutoBindViewPagerAdapter _adapter=new AutoBindViewPagerAdapter(viewPager.getContext(), ds,layoutId);
				if(this.parentViewAutoBinder!=null
						&&this.parentViewAutoBinder.getDefineCollection()!=null
						&&_adapter.GetViewAutoBinder()!=null)
					_adapter.GetViewAutoBinder().setDefineCollection(this.parentViewAutoBinder.getDefineCollection());
				viewPager.setAdapter(_adapter);
			}
		}
		else
		{
			AutoBindViewPagerAdapter _adapter=null;
			if(adapter instanceof AutoBindViewPagerAdapter)
			{
				_adapter=(AutoBindViewPagerAdapter) adapter;
				if(this.parentViewAutoBinder!=null
						&&this.parentViewAutoBinder.getDefineCollection()!=null
						&&_adapter.GetViewAutoBinder()!=null)
					_adapter.GetViewAutoBinder().setDefineCollection(this.parentViewAutoBinder.getDefineCollection());
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public HashMap<String, Object> GetBindingConfig(TypedArray ta) {
		return new HashMap<String, Object> ();
	}

	@Override
	public void PassParentViewAutoBinder(ViewAutoBinder viewAutoBinder) {
		this.parentViewAutoBinder=viewAutoBinder;
		
	}

}
