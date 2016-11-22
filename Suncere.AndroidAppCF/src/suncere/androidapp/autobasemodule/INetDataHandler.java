package suncere.androidapp.autobasemodule;

public interface INetDataHandler {

	void SetContext( NetDataHandlerContext context);
	
	void HandleNetData(AutoBaseModel model);
}
