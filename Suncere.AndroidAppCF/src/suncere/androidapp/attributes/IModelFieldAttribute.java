package suncere.androidapp.attributes;

import java.util.HashMap;

import suncere.androidapp.autobasemodule.AutoBaseModel;

public interface IModelFieldAttribute extends IAttribute {

	String FieldName(String value);
	
	String FieldName();
	
	int Rank();
	
	void HandleData(AutoBaseModel model,HashMap<String,Object> resultRowData);
	
	AttributeUsage AttributeUsage();
	
	AttributeUsage AttributeUsage( AttributeUsage value );
	
	
}
