package suncere.androidapp.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.DictionaryHelper;
import suncere.androidappcf.tools.TypeHelper;

public abstract class ConvertableAttribute implements IModelFieldAttribute{

	private String fieldName;
	protected String fieldNewName;
	protected AttributeUsage usage;

	public int Rank()
	{
		return 200;
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

	public void HandleData(AutoBaseModel model,HashMap<String,Object> resultRowData)
	{
		Method getter= TypeHelper.Getter(model.getClass(), this.FieldName());
		if(getter!=null)
		{
			try {
				Object fieldValue= getter.invoke(model, (Object[]) null);
				Object fieldNewValue= this.ConvertData(fieldValue, model);
				///一般在ResultDataHandel发生
				if(resultRowData!=null)
				{
//					if(resultRowData.containsKey(fieldNewName))
//						resultRowData.remove(fieldNewName);
//					resultRowData.put(fieldNewName, fieldNewValue);
					DictionaryHelper.OverrideAdd(resultRowData, fieldNewName, fieldNewValue);
				}
				///一般在NetDataHandle发生
//				else {
				Method setterMethod=TypeHelper.Setter(model.getClass(), fieldNewName);
				if(setterMethod!=null)
					setterMethod.invoke(model, fieldNewValue);
//				}
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
		return usage;
	}

	@Override
	public suncere.androidapp.attributes.AttributeUsage AttributeUsage(
			suncere.androidapp.attributes.AttributeUsage value) {
		this.usage=value;
		return null;
	}

	public abstract  Object ConvertData(Object value, AutoBaseModel model);

}
