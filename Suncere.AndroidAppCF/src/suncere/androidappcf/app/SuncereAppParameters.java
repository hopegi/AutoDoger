package suncere.androidappcf.app;

import java.io.IOException;

import suncere.androidappcf.tools.IniUtil;

public abstract class SuncereAppParameters {

	
	public static String IniFile;
	
	protected static IniUtil iniFile=null;
	
	protected static IniUtil GetIniFile()
	{
		if(iniFile==null)
		{
			try {
				iniFile=new IniUtil(IniFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return iniFile;
	}
	
	private static SuncereAppParameters _default;
	
	protected static  Class<?> Class()
	{
		return SuncereAppParameters.class;
	}
	
	public static SuncereAppParameters Init(Class<?>  Class )
	{
		if(_default==null)
		{
			try {
				_default=(SuncereAppParameters) Class.newInstance();
				_default.InnerIniParameters();
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		}
		return _default;
	}

	protected  abstract void  IniParameters();
	
	private void InnerIniParameters()
	{
		if(GetIniFile()!=null)
		{
			String tempString="";
			tempString=iniFile.getValue("ini", "IsFirstTime");
			if(tempString!=null&&tempString!="")IsFirstTime=tempString.toLowerCase().equals("true");
			else IsFirstTime=true;
		}
		_default.IniParameters();
	}
	
	public static String ServerIpPort;
	
	public static String UserName;
	
	public static String Password;
	
	public static int DbVersion;
	
	public static String DbName;
	
	public static String AssetsDbFileName;
	
	public static String MainCityName;
	
	public static boolean IsFirstTime;
	
	public static void SetServerIpPort(String value)
	{
		ServerIpPort=value;
		if(GetIniFile()!=null)
		{
			
				iniFile.putValue("ini", "ipport", ServerIpPort);
				try {
					iniFile.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void SetUserName(String value)
	{
		UserName=value;
		if(GetIniFile()!=null)
		{
			
				iniFile.putValue("ini", "UserName", UserName);
				try {
					iniFile.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void SetPassword(String value)
	{
		Password=value;
		if(GetIniFile()!=null)
		{
			
				iniFile.putValue("ini", "Password", Password);
				try {
					iniFile.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static void SetIsFirstTime(boolean value)
	{
		IsFirstTime=value;
		if(GetIniFile()!=null)
		{
			
				iniFile.putValue("ini", "IsFirstTime", value+"");
				try {
					iniFile.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
