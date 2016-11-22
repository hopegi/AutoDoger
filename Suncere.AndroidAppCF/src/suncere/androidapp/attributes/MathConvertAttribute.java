package suncere.androidapp.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.TypeHelper;

public class MathConvertAttribute extends ConvertableAttribute {

	public enum MathOption
	{
		//¾ø¶ÔÖµ
		Abs,
		//¼Ó
		Plus,
		//¼õ,
		Sub,
		BeSub,
		//³Ë
		Mulit,
		//³ý
		Div,
		BeDiv,
		
		Avg,
		Sum,
	}
	
	private MathOption mathOption;
	private Double number;
	private String[] fieldNameArr;
	
	public MathConvertAttribute(String fieldNewName, MathOption mathOption,Double number)
	{
		this(fieldNewName, mathOption, number, AttributeUsage.ResultDataHandle);
	}
	
	public MathConvertAttribute(String fieldNewName, MathOption mathOption,Double number,AttributeUsage usage)
	{
		this.fieldNewName=fieldNewName;
		this.mathOption=mathOption;
		this.number=number;
		this.usage=usage;
	}
	
	public MathConvertAttribute(String fieldNewName,MathOption mathOption,String[] fieldName)
	{
		this(fieldNewName, mathOption, fieldName, AttributeUsage.ResultDataHandle);
	}
	
	public MathConvertAttribute(String fieldNewName, MathOption mathOption,String[] fieldName,AttributeUsage usage)
	{
		this.fieldNewName=fieldNewName;
		this.mathOption=mathOption;
		this.fieldNameArr=fieldName;
		this.usage=usage;
	}
	
	@Override
	public Object ConvertData(Object value, AutoBaseModel model) {

		switch (mathOption) {
		case Abs:
			return this.Abs(value);
		case Plus:
			return Double.parseDouble(value.toString())+number;

		case Mulit:
			return Double.parseDouble(value.toString())*number;

		case Sub:
			return Double.parseDouble(value.toString())-number;

		case BeSub:
			return number- Double.parseDouble(value.toString());

		case Div:
			return Double.parseDouble(value.toString())/number;

		case BeDiv:
			return number/Double.parseDouble(value.toString());

		case Sum:
			return Sum(model);

		case Avg:
			return Sum(model);

		
		default:
			break;
		}
		return null;
	}

	private Object Abs(Object value)
	{
		if(TypeHelper.IsSubClassOf(value.getClass(), Integer.class))
		{
			return Math.abs( Integer.parseInt(value.toString())  );
		}
		if(TypeHelper.IsSubClassOf(value.getClass(), Float.class))
		{
			return Math.abs(  Float.parseFloat (value.toString())  );
		}
		if(TypeHelper.IsSubClassOf(value.getClass(), Long.class))
		{
			return Math.abs( Long.parseLong(value.toString())  );
		}
		if(TypeHelper.IsSubClassOf(value.getClass(), Double.class))
		{
			return Math.abs( Double.parseDouble(value.toString())  );
		}
		else if(TypeHelper.IsSubClassOf(value.getClass(), String.class))
		{
			String strVal=value.toString();
			if(strVal.contains("."))
				return Math.abs( Double.parseDouble(value.toString())  )+"";
			else 
				return Math.abs( Integer.parseInt(value.toString())  )+"";
		}
		return value;
	}

	private Object Sum(AutoBaseModel model)
	{
		double result=0;
		Object curr;
		Method getter;
		for (int i = 0; i < fieldNameArr.length; i++) {
			getter=TypeHelper.Getter(model.getClass(), fieldNameArr[i]);
			try {
				curr= getter.invoke(model, null);
				if(TypeHelper.IsSubClassOf(curr, String.class))
				{
					result+=Double.parseDouble(curr.toString());
				}
				else {
					result+=(Double)curr;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Object Avg(AutoBaseModel model)
	{
		double result=0;
		Object curr;
		Method getter;
		int count=0;
		for (int i = 0; i < fieldNameArr.length; i++) {
			getter=TypeHelper.Getter(model.getClass(), fieldNameArr[i]);
			try {
				curr= getter.invoke(model, null);
				if(TypeHelper.IsSubClassOf(curr, String.class))
				{
					result+=Double.parseDouble(curr.toString());
				}
				else {
					result+=(Double)curr;
				}
				count++;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return result/count;
	}
}
