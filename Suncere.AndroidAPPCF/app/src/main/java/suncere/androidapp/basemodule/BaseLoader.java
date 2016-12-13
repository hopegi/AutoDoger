package suncere.androidapp.basemodule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

//import org.apache.http.util.ByteArrayBuffer;
//import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import suncere.androidappcf.app.SuncereAppParameters;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.InputStreamUtil;
import suncere.androidappcf.tools.JSONHelper;
import suncere.androidappcf.tools.NetworkUitl;

public abstract class BaseLoader {
	// /服务端ip地址和密码
	protected String ServerIpPort() {
		return "http://" + SuncereAppParameters.ServerIpPort + "/api/";
	}

	// api的完整URL
	protected abstract String APIURL();

	// /用GET获取数据，获取数据是type类型
	protected <T> T GetLoadData(String url, Class<?> type) {
		T result = null;
		String json = null;
		try {
			InputStream is = NetworkUitl.GetStreamByHttpConnection(url);
			json = InputStreamUtil.InputStream2String(is);
			result = (T) JSONHelper.parseObject(json, type);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (json != null)
				result = this.Convert2SimpleType(json, type);
		}
		return result;
	}

	// /ykj get
	protected String GetLoadData(final Context context, String url, int timeout) {
		Object result = null;
		String json = null;

		try {
			InputStream is = NetworkUitl
					.GetStreamByHttpConnection(url, timeout);
			json = InputStreamUtil.InputStream2String(is);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Test", e.getMessage());
			e.printStackTrace();
			new Handler(context.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "网络不给力，请重试!", Toast.LENGTH_SHORT)
							.show();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Test", e.getMessage());
			new Handler(context.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "网络不给力，请重试!", Toast.LENGTH_SHORT)
							.show();
				}
			});
			// if(json!=null)
			// result=this.Convert2SimpleType(json, type);
		}
		return json;
	}

	// /ykj post
	protected String PostLoadData(final Context context,String url, String data) {
		String json = null;
		try {
			InputStream is = NetworkUitl.GetStreamByPostHttpConnection(url,
					data);
			json = InputStreamUtil.InputStream2String(is);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			new Handler(context.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "网络不给力，请重试!", Toast.LENGTH_SHORT)
							.show();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new Handler(context.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "网络不给力，请重试!", Toast.LENGTH_SHORT)
							.show();
				}
			});

		}

		return json;
	}

	// /用GET获取数据，获取数据是type类型，超时时间为timeout，单位毫秒
	protected <T> T GetLoadData(String url, Class<?> type, int timeout) {
		T result = null;
		String json = null;
		try {
			InputStream is = NetworkUitl
					.GetStreamByHttpConnection(url, timeout);
			json = InputStreamUtil.InputStream2String(is);
			result = (T) JSONHelper.parseObject(json, type);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (json != null)
				result = this.Convert2SimpleType(json, type);
		}
		return result;
	}

	// /用GET获取数组数据，获取元素的数据类型为type，超时时间为timeout，单位毫秒
	protected <T> T GetLoadArrayData(String url, Class<?> type, int timeout) {
		T result = null;
		try {
			Log.d("Test", "Start");
			InputStream is = NetworkUitl
					.GetStreamByHttpConnection(url, timeout);
			String json = InputStreamUtil.InputStream2String(is);
			Log.d("JSON", json);
			result = (T) JSONHelper.parseArray(json, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Test", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	// /用GET获取数组数据，获取元素的数据类型为type
	protected <T> T GetLoadArrayData(String url, Class<?> type) {
		T result = null;
		try {
			Log.d("Test", "Start");
			InputStream is = NetworkUitl.GetStreamByHttpConnection(url);
			String json = InputStreamUtil.InputStream2String(is);
			Log.d("JSON", json);
			result = (T) JSONHelper.parseArray(json, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Log.d("Test", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Test", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	// /用POST获取数组数据，获取元素的数据类型为type
	protected <T> T PostLoadArrayData(String url, String data, Class<?> type) {
		T result = null;
		String json = null;
		try {
			Log.d("Test", "Start");
			InputStream is = NetworkUitl.GetStreamByPostHttpConnection(url,
					data);
			json = InputStreamUtil.InputStream2String(is);
			Log.d("JSON", json);
			// json="[{\"TimePoint\":\"2014-04-28T12:00:00\",\"CityName\":\"city1\",\"AQI\":\"10\",\"PrimaryPollutant\":\"SO2\",\"Quality\":\"良\",\"SO2\":\"100\",\"NO2\":\"100\",\"O3\":\"100\",\"CO\":\"100\",\"PM10\":\"100\",\"PM2_5\":\"100\"},{\"TimePoint\":\"2014-04-28T12:00:00\",\"CityName\":\"city2\",\"AQI\":\"10\",\"PrimaryPollutant\":\"SO2\",\"Quality\":\"良\",\"SO2\":\"100\",\"NO2\":\"100\",\"O3\":\"100\",\"CO\":\"100\",\"PM10\":\"100\",\"PM2_5\":\"100\"}]";

			result = (T) JSONHelper.parseArray(json, type);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (json != null)
				result = this.Convert2SimpleType(json, type);
		}

		return result;
	}

	// //给城市名列表编码
	protected String EncodCityNames(List<String> citys) {
		StringBuilder sb = new StringBuilder();
		for (String city : citys) {
			try {
				sb.append("&cities=" + URLEncoder.encode(city, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String result = sb.toString();
		if (result.length() != 0)
			result = result.substring(1);
		return result;
	}

	// /给字符串编码
	protected String EncodingString(String strValue) {
		String result = "";
		if (strValue == null || strValue.length() == 0)
			return result;
		try {
			result = URLEncoder.encode(strValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// /把字符串转换到指定的简单类型
	protected <T> T Convert2SimpleType(String value, Class<?> type) {
		if (type.equals(String.class))
			return (T) value;
		return null;
	}
}
