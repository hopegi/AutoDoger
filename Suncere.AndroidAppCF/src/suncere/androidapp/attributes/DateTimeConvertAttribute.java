package suncere.androidapp.attributes;

import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.DateTimeTool;

public class DateTimeConvertAttribute extends ConvertableAttribute {

	private String formatStr;
	
	public DateTimeConvertAttribute(String fieldNewName,String formatStr)
	{
//		super.fieldNewName=fieldNewName;
//		this.formatStr=formatStr;
		this(fieldNewName, formatStr, AttributeUsage.ResultDataHandle);
	}
	
	public DateTimeConvertAttribute(String fieldNewName,String formatStr,AttributeUsage usage)
	{
		super.fieldNewName=fieldNewName;
		this.formatStr=formatStr;
		this.usage=usage;
	}
	
	@Override
	public Object ConvertData(Object value, AutoBaseModel model) {
		return  DateTimeTool.ToString( DateTimeTool.Prase(value),formatStr);
	}

}
