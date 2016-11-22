package suncere.androidapp.autobasemodule;

import java.util.HashMap;

public class PluginContext {

	private IChecker checker;
	private AutoBaseDAL mainDal;
	private AutoBaseModel model;
	private AutoBaseDataLoader loader;
	private INetDataHandler netDataHandler;
	private IResultDataHandler resultDataHandler;
	
	public IResultDataHandler getResultDataHandler() {
		return resultDataHandler;
	}

	public void setResultDataHandler(IResultDataHandler resultDataHandler) {
		this.resultDataHandler = resultDataHandler;
	}

	public INetDataHandler getNetDataHandler() {
		return netDataHandler;
	}

	public void setNetDataHandler(INetDataHandler netDataHandler) {
		this.netDataHandler = netDataHandler;
	}

	public AutoBaseDataLoader getLoader() {
		return loader;
	}

	public void setLoader(AutoBaseDataLoader loader) {
		this.loader = loader;
	}

	public IChecker getChecker() {
		return checker;
	}

	public void setChecker(IChecker checker) {
		this.checker = checker;
	}

	public AutoBaseDAL getMainDal() {
		return mainDal;
	}

	public void setMainDal(AutoBaseDAL mainDal) {
		this.mainDal = mainDal;
	}

	public AutoBaseModel getModel() {
		return model;
	}

	public void setModel(AutoBaseModel model) {
		this.model = model;
	}
	
	public CheckerContext BuildCheckerContext()
	{
		CheckerContext result=new CheckerContext();
		result.setDal(getMainDal());
		result.setModel(getModel());
		return result;
	}
	
	public NetDataHandlerContext BuildNetDataHandlerContext(HashMap<String,Object> otherPara)
	{
		NetDataHandlerContext result=new NetDataHandlerContext();
		result.setModel(getModel());
		result.setOtherParameters(otherPara);
		return result;
	}
}
