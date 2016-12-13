package suncere.androidapp.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.R.string;
import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.TypeHelper;

public class FormatConvertAttribute  extends ConvertableAttribute{

	private  String[] fieldNameArr;
	private String formatter;
	
	public FormatConvertAttribute(String fieldNewName , String formatter,String[] fieldNameArr)
	{
		this(fieldNewName,formatter, fieldNameArr, AttributeUsage.ResultDataHandle);
	}
	
	public FormatConvertAttribute(String fieldNewName , String formatter,String[] fieldNameArr,AttributeUsage usage  )
	{
		this.fieldNewName=fieldNewName;
		this.fieldNameArr=fieldNameArr;
		this.formatter=formatter;
		this.usage=usage;
	}

	@Override
	public Object ConvertData(Object value, AutoBaseModel model) {
		Object[] valueArr=new Object[ fieldNameArr.length ];
		Method getter;
		for (int i = 0; i < fieldNameArr.length; i++) {
			getter=TypeHelper.Getter(model.getClass(), fieldNameArr[i]);
			try {
				valueArr[i]=getter.invoke(model, (Object[]) null);
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return String.format(formatter,  valueArr);
		
	}

}
