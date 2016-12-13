package suncere.androidapp.viewautobinder;

import java.util.HashMap;
import java.util.List;

import android.content.res.TypedArray;
import android.view.View;

public interface IViewBinder {

	List<String> SupportAttrsName();
	
	Class<?> SupportViewType();
	
	void BindViewValue(HashMap<String, Object> values, View view);
	
	HashMap<String,Object> GetBindingConfig(TypedArray ta);
	
}
