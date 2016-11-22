package suncere.androidapp.autobasemodule;

import java.util.HashMap;

public interface IPluginLoadingEvent {

	HashMap<Integer, Class<?>> OnLoadStatementProvider();
	
	AutoBaseDataLoader GetLoader();
	
	AutoBaseDAL GetMainDAL();
	
	IChecker GetChecker();
	
	INetDataHandler GetNetDataHandler();
	
	IResultDataHandler GetResultDataHandler();
	
//	AutoBaseBLL GetBLL();
}
