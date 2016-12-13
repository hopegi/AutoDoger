package suncere.androidapp.attributes;

import java.util.List;

import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.aqi.AQITool;
import suncere.androidappcf.aqi.AQITool.AQIPresentEnum;
import suncere.androidappcf.aqi.AQITool.AQITypedArrayType;
import suncere.androidappcf.tools.TypeHelper;

public class AQIResConvertAttribute extends ConvertableAttribute{


	public final static int AQI=1;
	public final static int Quality=2;
	public final static int Chinese=3;
	
	private AQIPresentEnum aqiPresentEnum ;
	private List<Object>resourceLst;
	private int valueType;
	private boolean isCustom=false;
	
	private int resId=-1;
	private AQITypedArrayType arrayType=null;
	

	public AQIResConvertAttribute(String fieldNewName,int valueType,Object resultRes)
	{
		this(fieldNewName, valueType, resultRes, AttributeUsage.ResultDataHandle);
	}


	public AQIResConvertAttribute(String fieldNewName,int valueType,Object resultRes,AttributeUsage usage)
	{
		super.fieldNewName=fieldNewName;
		this.valueType=valueType;
		this.usage=usage;
		if(TypeHelper.IsSubClassOf(resultRes, AQIPresentEnum.class))
		{
			this.aqiPresentEnum=(AQIPresentEnum) resultRes;
			this.isCustom=false;
		}
		else
		{
			this.resourceLst=(List<Object>) resultRes;
			this.isCustom=true;
		}
	}
	

	public AQIResConvertAttribute(String fieldNewName,int valueType,int res, AQITypedArrayType arrayType ,AttributeUsage usage)
	{
		super.fieldNewName=fieldNewName;
		this.valueType=valueType;
		this.usage=usage;
		this.resId=res;
		this.arrayType=arrayType;
	}
	
	public AQIResConvertAttribute(String fieldNewName,int valueType,int res, AQITypedArrayType arrayType)
	{
		this(fieldNewName, valueType, res,arrayType,AttributeUsage.ResultDataHandle);
	}
	
	
	@Override
	public Object ConvertData(Object value, AutoBaseModel model) {
		

		if(this.arrayType!=null)
		{
			if(this.valueType==this.AQI)
			{
				return AQITool.GetResourceByAQI(value, this.resId, this.arrayType);
			}
			else if(this.valueType==this.Chinese)
			{
				int aqNumber=(Integer) AQITool.GetAQIResourceByChineseLevel(value.toString(), AQIPresentEnum.ChineseLevel);
				return AQITool.GetResourceByAQ(aqNumber, resId, arrayType);
			}
			else
			{
				return AQITool.GetResourceByAQ(value, resId, arrayType);
			}
		}

		else if(this.isCustom)
		{
			Object defaultRes=this.resourceLst.size()>6?this.resourceLst.get(6):this.resourceLst.get(0);
			if(this.valueType==this.AQI)
			{
				return AQITool.GetCustomResourceByAQI(value.toString(), this.resourceLst, defaultRes);
			}
			else if(this.valueType==this.Chinese)
			{
				return AQITool.GetCustomResourceByChineseLevel(value.toString(), this.resourceLst, defaultRes);
			}
			else
			{
				return AQITool.GetCustomResourceByAQ(value.toString(), this.resourceLst, defaultRes);
			}
		}
		else
		{
			if(this.valueType==this.AQI)
			{
				return AQITool.GetAQIResourceByAQI(value.toString(), aqiPresentEnum);
			}
			else if(this.valueType==this.Chinese)
			{
				return AQITool.GetAQIResourceByChineseLevel(value.toString(), this.aqiPresentEnum);
			}
			else
			{
				return AQITool.GetAQIResourceByAQ(value.toString(), aqiPresentEnum);
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
