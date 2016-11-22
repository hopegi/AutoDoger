package suncere.androidapp.viewautobinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.R;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageViewBinder  implements IViewBinder {

	private String[] supportAttrsNameArr=new String[]{ "src" };
	
	@Override
	public List<String> SupportAttrsName() {
		return  Arrays.asList( supportAttrsNameArr);
	}

	@Override
	public Class<?> SupportViewType() {
		// TODO Auto-generated method stub
		return ImageView.class;
	}

	@Override
	public void BindViewValue(HashMap<String, Object> values, View view) {
		if(!(view instanceof ImageView))
		{
			Log.d("", " 给定View不是ImageView 及其子类  ");
			return;
		}
		ImageView img=(ImageView)view;
		if(values.containsKey("src"))
		{
			Object value=values.get("src");
			if(value instanceof Drawable)
				img.setImageDrawable((Drawable)value);
			if(value instanceof Bitmap)
				img.setImageBitmap((Bitmap)value);
			if(value instanceof Integer)
				img.setImageResource(Integer.parseInt(value.toString()));
			if(value instanceof String)
				img.setImageBitmap(BitmapFactory.decodeFile(value.toString()));
		}
	}

	@Override
	public HashMap<String, Object> GetBindingConfig(TypedArray ta) {

		HashMap<String,Object> result=new HashMap<String,Object>();
		
		String tmp;
		tmp=ta.getString(R.styleable.View_src);
		if(tmp!=null&&tmp.length()>0)
		result.put("src", tmp);
		
		return result;
	}

}
