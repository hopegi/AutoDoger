package suncere.androidapp.viewautobinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.R;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

///通过BindData方法绑定数据的View
public class BindDataViewBinder implements IViewBinder {

	private String[] supportAttrsNameArr=new String[]{ "BindData" };
	
	@Override
	public List<String> SupportAttrsName() {
		return  Arrays.asList( supportAttrsNameArr);
	}

	@Override
	public Class<?> SupportViewType() {
		///若有Chart这个类 应转换为该类
		return LinearLayout.class;
	}

	@Override
	public void BindViewValue(HashMap<String, Object> values, View view) {
		if(values.containsKey("BindData"))
		{
			Object para=values.get("BindData");
			try {
				Method bindDataMethod= view.getClass().getDeclaredMethod("BindData",List.class);
				bindDataMethod.invoke(view, para);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public HashMap<String, Object> GetBindingConfig(TypedArray ta) {
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		String tmp;
		tmp=ta.getString(R.styleable.View_BindData);
		if(tmp!=null&&tmp.length()>0)
			result.put("BindData", tmp);
		
		return result;
	}

}
