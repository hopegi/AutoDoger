package suncere.androidapp.autobasemodule;

import java.util.HashMap;

public class AlwaysUpdateChecker implements IChecker{

	@Override
	public boolean ExistData(HashMap<String, Object> parameter) {
		return false;
	}

	@Override
	public void SetContext(CheckerContext context) {
	}
	
}
