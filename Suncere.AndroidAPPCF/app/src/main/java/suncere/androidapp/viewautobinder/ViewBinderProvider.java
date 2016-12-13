package suncere.androidapp.viewautobinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidappcf.tools.TypeHelper;
import android.util.Log;
import android.view.View;

///没有线程安全
public class ViewBinderProvider {

	private static ViewBinderProvider _default;
	
	private HashMap<String,IViewBinder> binderCollection;
	
	private ViewBinderProvider()
	{
		binderCollection=new HashMap<String,IViewBinder>();
	}
	
	public static ViewBinderProvider Current()
	{
		if(_default==null)
			_default=new ViewBinderProvider();
		return _default;
	}
	
	public void AddViewBinder(IViewBinder binder)
	{
		if(binder==null)
		{
			Log.d("", "binder is null");
			return;
		}
		this.AddViewBinder(binder.getClass().getSimpleName(), binder);
	}
	
	public void AddViewBinder(String name, IViewBinder binder)
	{
		if(name==null)
		{
			Log.d("", "name is null");
			return;
		}
		if(binder==null)
		{
			Log.d("", "binder is null");
			return;
		}
		if(binderCollection.containsKey(name))
		{
			Log.d("", "name is been overrite  ");
			binderCollection.remove(name);
		}
		binderCollection.put(name, binder);
	}

	public List<IViewBinder> GetViewBinder(View v)
	{
		List<IViewBinder> result=new ArrayList<IViewBinder>();
		
		for(Entry<String,IViewBinder> kvp : binderCollection.entrySet())
		{
			if(TypeHelper.IsSubClassOf(v, kvp.getValue().SupportViewType()))
				result.add(kvp.getValue());
		}
		return result;
	}
	
	public List<IViewBinder> GetAllViewBinder()
	{
		List<IViewBinder> result=new ArrayList<IViewBinder>();
		
		result.addAll(  binderCollection.values()  );
		
		return result;
	}
}
