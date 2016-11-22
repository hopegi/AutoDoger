package suncere.androidappcf.tools;

import java.util.HashMap;

import suncere.androidapp.autobasemodule.AutoBaseModel;

public class DictionaryHelper {

	public static void OverrideAdd(HashMap<String,  Object> dic,String key,Object value)
	{
		if(dic.containsKey(key))
		{
			dic.remove(key);
		}
		dic.put(key, value);
	}
}
