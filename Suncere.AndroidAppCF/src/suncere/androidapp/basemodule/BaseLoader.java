package suncere.androidapp.basemodule;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.widget.Toast;
import suncere.androidappcf.app.SuncereAppParameters;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.InputStreamUtil;
import suncere.androidappcf.tools.JSONHelper;
import suncere.androidappcf.tools.NetworkUitl;

public abstract class BaseLoader 
{
	///�����ip��ַ������
	protected  String ServerIpPort()
	{
		return "http://"+SuncereAppParameters.ServerIpPort+"/api/";
	}
	
	//api������URL
	protected abstract String APIURL();
	
	///��GET��ȡ���ݣ���ȡ������type����
	protected <T> T GetLoadData(String url,Class<?> type)
	{
		T result=null;
		String json=null;
		try {
			InputStream is= NetworkUitl.GetStreamByHttpConnection(url);
			json=InputStreamUtil.InputStream2String(is);
			 result=(T)JSONHelper.parseObject(json, type);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(json!=null)
				result=this.Convert2SimpleType(json, type);
		}
		return result;
	}
	
	///��GET��ȡ���ݣ���ȡ������type���ͣ���ʱʱ��Ϊtimeout����λ����
	protected <T> T GetLoadData(String url,Class<?> type,int timeout)
	{
		T result=null;
		String json=null;
		try {
			InputStream is= NetworkUitl.GetStreamByHttpConnection(url,timeout);
			json=InputStreamUtil.InputStream2String(is);
			 result=(T)JSONHelper.parseObject(json, type);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(json!=null)
				result=this.Convert2SimpleType(json, type);
		}
		return result;
	}

	///��GET��ȡ�������ݣ���ȡԪ�ص���������Ϊtype����ʱʱ��Ϊtimeout����λ����
	protected <T> T GetLoadArrayData(String url,Class<?> type,int timeout)
	{
		T result=null;
		try {
			Log.d("Test","Start");
			InputStream is= NetworkUitl.GetStreamByHttpConnection(url,timeout);
			String json=InputStreamUtil.InputStream2String(is);
			Log.d("JSON", json);
			 result=(T)JSONHelper.parseArray(json, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Test", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	///��GET��ȡ�������ݣ���ȡԪ�ص���������Ϊtype
	protected <T> T GetLoadArrayData(String url,Class<?> type)
	{
		T result=null;
		try {
			Log.d("Test","Start");
			InputStream is= NetworkUitl.GetStreamByHttpConnection(url);
			String json=InputStreamUtil.InputStream2String(is);
			Log.d("JSON", json);
			 result=(T)JSONHelper.parseArray(json, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Test", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	///��POST��ȡ�������ݣ���ȡԪ�ص���������Ϊtype
	protected <T> T PostLoadArrayData(String url,String data,Class<?> type)
	{
		T result=null;
		String json=null;
		try {
			Log.d("Test","Start");
			InputStream is= NetworkUitl.GetStreamByPostHttpConnection(url, data);
			json=InputStreamUtil.InputStream2String(is);
			Log.d("JSON", json);
//			json="[{\"TimePoint\":\"2014-04-28T12:00:00\",\"CityName\":\"city1\",\"AQI\":\"10\",\"PrimaryPollutant\":\"SO2\",\"Quality\":\"��\",\"SO2\":\"100\",\"NO2\":\"100\",\"O3\":\"100\",\"CO\":\"100\",\"PM10\":\"100\",\"PM2_5\":\"100\"},{\"TimePoint\":\"2014-04-28T12:00:00\",\"CityName\":\"city2\",\"AQI\":\"10\",\"PrimaryPollutant\":\"SO2\",\"Quality\":\"��\",\"SO2\":\"100\",\"NO2\":\"100\",\"O3\":\"100\",\"CO\":\"100\",\"PM10\":\"100\",\"PM2_5\":\"100\"}]";
			
			 result=(T)JSONHelper.parseArray(json, type);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(json!=null)
				result=this.Convert2SimpleType(json, type);
		}

		return result;
	}

	////���������б����
	protected String EncodCityNames(List<String> citys)
	{
		StringBuilder sb=new StringBuilder();
		for(String city:citys){
			try {
				sb.append("&cities="+URLEncoder.encode(city, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String result=sb.toString();
		if(result.length()!=0)
			result=result.substring(1);
		return result;
	}
	
	///���ַ�������
	protected String EncodingString(String strValue)
	{
		String result="";
		if(strValue==null||strValue.length()==0)return result;
		try {
			result=URLEncoder.encode(strValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	///���ַ���ת����ָ���ļ�����
	protected <T> T Convert2SimpleType(String value,Class<?> type)
	{
		if(type.equals(String.class))
			return  (T) value;
		return null;
	}
}
