package suncere.androidapp.autobasemodule;

import java.util.HashMap;

public class NetDataHandlerContext {

	private  AutoBaseModel model;
	private  HashMap<String,Object> otherParameters;
	
	public AutoBaseModel getModel() {
		return model;
	}
	public void setModel(AutoBaseModel model) {
		this.model = model;
	}
	public HashMap<String, Object> getOtherParameters() {
		return otherParameters;
	}
	public void setOtherParameters(HashMap<String, Object> otherParameters) {
		this.otherParameters = otherParameters;
	}
}
