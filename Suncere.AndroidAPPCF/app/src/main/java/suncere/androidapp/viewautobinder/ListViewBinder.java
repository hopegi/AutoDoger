package suncere.androidapp.viewautobinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.R;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewBinder implements IViewBinder ,IChildViewAutoBinder{

	private String[] supportAttrsNameArr=new String[]{ "layout","datasource","adapter" };
	private ViewAutoBinder parentViewAutoBinder;
	
	@Override
	public List<String> SupportAttrsName() {
		return Arrays.asList( supportAttrsNameArr);
	}

	@Override
	public Class<?> SupportViewType() {
		return ListView.class;
	}

	@Override  
	public void BindViewValue(HashMap<String, Object> values, View view) {
		if(!(view instanceof ListView))
		{
			Log.d("", " 给定View不是TextView 及其子类  ");
			return;
		}
		ListView listView=(ListView)view;
		SimpleAdapter adapter=(SimpleAdapter) listView.getAdapter();
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
				adapter=(AutoBindListViewAdapter) values.get("adapter");
				listView.setAdapter(adapter);
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
				AutoBindListViewAdapter _adapter= AutoBindListViewAdapter.BuildAdapter(listView.getContext(), ds,layoutId);
				if(this.parentViewAutoBinder!=null
						&&this.parentViewAutoBinder.getDefineCollection()!=null
						&&_adapter.GetViewAutoBinder()!=null)
					_adapter.GetViewAutoBinder().setDefineCollection(this.parentViewAutoBinder.getDefineCollection());
				listView.setAdapter(_adapter);
			}
		}
		else
		{
			AutoBindListViewAdapter _adapter=null;
			if(adapter instanceof AutoBindListViewAdapter)
			{
				_adapter=(AutoBindListViewAdapter) adapter;
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
		HashMap<String,Object> result=new HashMap<String,Object>();
		
		String tmp=null;
		tmp=ta.getString(R.styleable.View_adapter);
		if(tmp!=null&&tmp.length()>0)
			result.put("adapter", tmp);
		int tmpInt= ta.getResourceId(R.styleable.View_layout, -1);
		if(tmpInt!=-1)
			result.put("layout", tmpInt);
		tmp=ta.getString(R.styleable.View_datasource);
		if(tmp!=null&&tmp.length()>0)
			result.put("datasource", tmp);
		
		return result;
	}

	@Override
	public void PassParentViewAutoBinder(ViewAutoBinder viewAutoBinder) {
		this.parentViewAutoBinder=viewAutoBinder;
	}

}
