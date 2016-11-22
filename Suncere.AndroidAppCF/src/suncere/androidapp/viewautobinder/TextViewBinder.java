package suncere.androidapp.viewautobinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.R;
import suncere.androidappcf.controls.PollutantNameTextView;
import suncere.androidappcf.tools.TypeHelper;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TextViewBinder  implements IViewBinder {

	private String[] supportAttrsNameArr=new String[]{ "text","textColor","textColorRes" };
	
	@Override
	public List<String> SupportAttrsName() {
		return  Arrays.asList( supportAttrsNameArr);
	}

	@Override
	public Class<?> SupportViewType() {
		return TextView.class;
	}

	@Override
	public void BindViewValue(HashMap<String, Object> values, View view) {
		if(!(view instanceof TextView))
		{
			Log.d("", " 给定View不是TextView 及其子类  ");
			return;
		}
		TextView textView=(TextView)view;
		PollutantNameTextView ptextView=null;
		if(textView instanceof PollutantNameTextView )
			ptextView=(PollutantNameTextView) textView;
		if(values.containsKey("text"))
		{
			Object value=values.get("text");
			if(ptextView==null)
			{
				if(value instanceof String)
					textView.setText(value.toString());
				if(TypeHelper.IsNumberType(value))
					textView.setText(value.toString());
			}
			else
			{
				if(value instanceof String)
					ptextView.setText(value.toString());
				if(TypeHelper.IsNumberType(value))
					ptextView.setText(value.toString());
			}
		}
		else if(values.containsValue("textRes"))
		{
			Object value=values.get("textRes");
			if(ptextView!=null)
			{
				if(value instanceof Integer)
					ptextView.setText( ptextView.getResources().getText(  Integer.parseInt(value.toString())));
			}
			else
			{
				if(value instanceof Integer)
					textView.setText( textView.getResources().getText(  Integer.parseInt(value.toString())));
			}
		}
		if(values.containsKey("textColor"))
		{
			Object value=values.get("textColor");
			if(value instanceof Integer)
				textView.setTextColor(Integer.parseInt(value.toString()));
		}
		if(values.containsKey("textColorRes"))
		{
			Object value=values.get("textColorRes");
			if(value instanceof Integer)
				textView.setTextColor( textView.getResources().getColor(  Integer.parseInt(value.toString())));
		}
	}

	@Override
	public HashMap<String, Object> GetBindingConfig(TypedArray ta) {
		HashMap<String, Object> result=new HashMap<String,Object>();
		
		String tmp=null;
		tmp=ta.getString(R.styleable.View_text);
		if(tmp!=null&&tmp.length()>0)
			result.put("text", tmp);
		tmp=ta.getString(R.styleable.View_textColor);
		if(tmp!=null&&tmp.length()>0)
			result.put("textColor", tmp);
		tmp=ta.getString(R.styleable.View_textColorRes);
		if(tmp!=null&&tmp.length()>0)
			result.put("textColorRes", tmp);
		tmp=ta.getString(R.styleable.View_textRes);
		if(tmp!=null&&tmp.length()>0)
			result.put("textRes", tmp);
		
		return result;
	}

}
