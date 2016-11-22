package suncere.androidapp.viewautobinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.R;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ViewBinder implements IViewBinder {

	private String[] supportAttrsNameArr=new String[]{ "background","backgroundColor" };
	
	@Override
	public List<String> SupportAttrsName() {
		return  Arrays.asList( supportAttrsNameArr);
	}

	@Override
	public Class<?> SupportViewType() {
		return View.class;
	}

	@Override
	public void BindViewValue(HashMap<String, Object> values, View view) {
		if(values.containsKey("background"))
		{
			Object value=values.get("background");
			if(value instanceof Drawable)
				view.setBackground((Drawable)value);
			if(value instanceof Integer)
				view.setBackgroundResource(Integer.parseInt(value.toString()));
		}
		else if(values.containsKey("backgroundColor"))
		{
			Object value=values.get("backgroundColor");
			if(value instanceof Integer)
				view.setBackgroundColor(Integer.parseInt(value.toString()));
		}
	}

	@Override
	public HashMap<String, Object> GetBindingConfig(TypedArray ta) {
		
		 HashMap<String, Object> result=new  HashMap<String, Object>();
		 
		 String tmp;
		 tmp=ta.getString(R.styleable.View_background);
		 if(tmp!=null&&tmp.length()>0)
			 result.put("background", tmp);
		 tmp=ta.getString(R.styleable.View_backgroundColor);
		 if(tmp!=null&&tmp.length()>0)
			 result.put("backgroundColor", tmp);
		 
		 return result;
	}

}
