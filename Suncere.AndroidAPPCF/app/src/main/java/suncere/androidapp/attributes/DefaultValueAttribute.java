package suncere.androidapp.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.DictionaryHelper;
import suncere.androidappcf.tools.TypeHelper;

public class DefaultValueAttribute implements IModelFieldAttribute {

	private String fieldName;
	private Object defaultValue;
	private AttributeUsage usage;
	private Object[] nullValues;
	
	private static Object[] defaultNullValues=new Object[]{""};
	
	public DefaultValueAttribute(Object defaultValue )
	{
		this(defaultValue, AttributeUsage.NetDataHandle);
		
	}
	
	public DefaultValueAttribute(Object defaultValue,Object... nullValues)
	{
		this(defaultValue, AttributeUsage.NetDataHandle,nullValues);
	}
	
	public DefaultValueAttribute(Object defaultValue,AttributeUsage usage )
	{
		this(defaultValue,usage,defaultNullValues);
	}
	
	public  DefaultValueAttribute(Object defaultValue,AttributeUsage usage,Object... nullValues )
	{
		this.defaultValue=defaultValue;
		this.usage=usage;
		this.nullValues=nullValues;
	}
	
	@Override
	public String FieldName(String value) {
		this.fieldName=value;
		return this.FieldName();
	}

	@Override
	public String FieldName() {
		return this.fieldName;
	}
	
	@Override
	public int Rank() {
		return 100;
	}
	
	@Override
	public void HandleData(AutoBaseModel model,
			HashMap<String, Object> resultRowData) {
		Method getter= TypeHelper.Getter(model.getClass(), this.FieldName());
		if(getter!=null)
		{
			 try {
				Object fieldValue= getter.invoke(model, (Object[]) null);
				//if(fieldValue==null||fieldValue.toString().length()==0)
				for(int i=0;i<this.nullValues.length;i++)
				{
					if(fieldValue==null||  fieldValue.equals(nullValues[i]) )
					{
						if(resultRowData!=null)
						{
							DictionaryHelper.OverrideAdd(resultRowData, this.FieldName(), defaultValue);
						}
						if(model!=null){
							Method setterMethod=TypeHelper.Setter(model.getClass(), this.FieldName());
							setterMethod.invoke(model, defaultValue);
						}
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public suncere.androidapp.attributes.AttributeUsage AttributeUsage() {
		return this.usage;
	}

	@Override
	public suncere.androidapp.attributes.AttributeUsage AttributeUsage(
			suncere.androidapp.attributes.AttributeUsage value) {
		this.usage=value;
		return null;
	}
}
