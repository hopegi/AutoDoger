package suncere.androidapp.viewautobinder;

import java.util.HashMap;

public abstract class BaseBinderComplier {

	public abstract void InitComplier(HashMap<String,Object> parameters);
	
	public abstract Object ComplieValue(Object statement);
}
