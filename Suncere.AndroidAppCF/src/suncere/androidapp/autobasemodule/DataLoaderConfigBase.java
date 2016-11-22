package suncere.androidapp.autobasemodule;

import java.util.HashMap;

import cn.jpush.android.util.ab;

public abstract class DataLoaderConfigBase {

	///GET����  ���ؼ���
	public static final int GET_ARRAY=1;
	///GET���� ���ص�������
	public static final int GET_SINGLE=2;
	///POST���� ���ؼ���
	public static final int POST_ARRAY=3;
//	///POST���� ���ص�������
//	public static final int POST_SINGLE=4;
	

	public final static String CONFIG_NAME="&ConfigName";
	
	private HashMap<String, Object> para;
	
	public HashMap<String, Object> getPara() {
		return para;
	}

	public void setPara(HashMap<String, Object> para) {
		this.para = para;
	}

	///���õı�ʶ��
	public String ConfigName() 
	{
		String className= this.getClass().getSimpleName();
		return className.replace("DataLoaderConfig", "");
//		return "default";
	}
	
	public boolean IsDefault()
	{
		return false;
	}
	
	///�����URL  ������  {controller}/{action} ����
	public abstract String APIURL() ;
	
	///����  ������ڶ�������Ĳ�����Object��Ϊ List���� Array
	public HashMap<String, Object> Parameters(AutoBaseModel model)
	{
//		return null;
		return para;
	}
	
	///��������
	public abstract int LoadDataType();
}
