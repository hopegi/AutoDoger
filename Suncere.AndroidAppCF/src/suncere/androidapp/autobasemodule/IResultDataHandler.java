package suncere.androidapp.autobasemodule;

import java.util.HashMap;
import java.util.List;

public interface IResultDataHandler {
	
	void SetContext(ResultDataHandlerContext context);
	
	void HandleResultData(List<HashMap<String,Object>> data);
}
