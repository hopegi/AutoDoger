package suncere.androidapp.autobasemodule;

import java.util.HashMap;

public interface IChecker {

	void SetContext(CheckerContext context);
	
	boolean ExistData(HashMap<String, Object> parameter);
}
