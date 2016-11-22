package suncere.androidappcf.tools;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import suncere.androidappcf.controls.SuncereApplication;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkUitl {

	public static List<Long> ErrorConnectionList=new ArrayList<Long>();
	
	public static InputStream GetStreamByHttpConnection(String urlString) throws IOException {
		return GetStreamByHttpConnection( urlString,0);
	}
	
	public static InputStream GetStreamByHttpConnection(String urlString,int timeout) throws IOException {
			
			InputStream result=null;
			int response=-1;
			
			URL url=new URL(urlString);
			URLConnection conn=url.openConnection();
			HttpURLConnection httpConn=null;
			
			if(!(conn instanceof HttpURLConnection))
				throw new IOException("Not an Http Connection");
			
			try {
				httpConn=(HttpURLConnection)conn;
				httpConn.setAllowUserInteraction(false);
				httpConn.setInstanceFollowRedirects(true);
				httpConn.setRequestMethod("GET");
				if(timeout>0)
				{
					httpConn.setConnectTimeout(timeout);
					httpConn.setReadTimeout(timeout);
				}
				httpConn.connect();
				response=httpConn.getResponseCode();
				if(response==HttpURLConnection.HTTP_OK)
					result=httpConn.getInputStream();
			} catch (Exception ex) {
				// TODO: handle exception
//				Log.d("EnvAppDemo", ex.getMessage());
				ErrorConnectionList.add(Thread.currentThread().getId());
				throw new IOException("Error Connecting ");
			}
			return result;
		}
	
	public static InputStream GetStreamByPostHttpConnection(String urlString,String param) throws IOException
	{
		PrintWriter out = null;
		InputStream result=null;
		int response=-1;
        try {
            URL realUrl = new URL(urlString);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������

            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setRequestProperty("Accept-Language", "zh-CN");
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Encoding", "UTF-8");
            
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            
            response=((HttpURLConnection) conn).getResponseCode();
			if(response==HttpURLConnection.HTTP_OK)
				result=conn.getInputStream();
            
        } catch (Exception e) {
            //System.out.println("���� POST ��������쳣��"+e);
//        	Log.d("Tag", "���� POST ��������쳣��"+e);
			ErrorConnectionList.add(Thread.currentThread().getId());
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
        finally{
            if(out!=null){
			    out.close();
			}
        }
        return result;
	}

	public static boolean DownLoadFile(String url,String fileFullName) throws IOException
	{
		boolean result=false;
		InputStream is=null;
		OutputStream os=null;
		try {
			is=NetworkUitl.GetStreamByHttpConnection(url);
			os=new FileOutputStream(fileFullName);
			int bufferSize=1024;
			int length=0;
			byte[] datas=new byte[bufferSize];
			while((length=is.read(datas, 0, bufferSize))>0)
			{
				os.write(datas,0,length);
			}
			is.close();
			os.close();
			result=true;
		} catch (IOException e) {
			
			result=false;
			throw e;
		}
		finally
		{
			if(is!=null)
			{
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static boolean IsReachable(InetAddress localInetAddr, InetAddress remoteInetAddr,int port, int timeout) {   

		boolean isReachable = false;   
		Socket socket = null;   
		try{   
				socket = new Socket();   
				// �˿ں�����Ϊ 0 ��ʾ�ڱ�����ѡһ�����ö˿ڽ�������  
				 SocketAddress localSocketAddr = new InetSocketAddress(localInetAddr, 0);   
//				 socket.bind(localSocketAddr);
				 InetSocketAddress endpointSocketAddr =   
				 new InetSocketAddress(remoteInetAddr, port);   
				 socket.connect(endpointSocketAddr, timeout);          
				 System.out.println("SUCCESS - connection established! Local: " +   
				   localInetAddr.getHostAddress() + " remote: " +   
				   remoteInetAddr.getHostAddress() + " port" + port);   
				 isReachable = true;   
				}
				catch(IOException e) {   
					 System.out.println("FAILRE - CAN not connect! Local: " +   
					localInetAddr.getHostAddress() + " remote: " +   
					remoteInetAddr.getHostAddress() + " port" + port);   
				} 
				finally{   
					 if(socket != null) {   
					 try{   
						 socket.close();   
					 } 
					 catch(IOException e) {   
					    System.out.println("Error occurred while closing socket..");   
					 }   
				 }   
			}   
			return isReachable;   
	}  

	public static boolean IsReachable(String localIntAddr,String remoteInetAddr ,int port,int timeout)
	{
		InetAddress local=null;
		InetAddress remote=null;
		try {
			local = InetAddress.getByName(localIntAddr);
			remote=InetAddress.getByName(remoteInetAddr);
			if(local!=null&&remote!=null)
				return IsReachable(local,remote,port,timeout);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		catch(Exception ex)
		{
			Log.d("TAG", "TAG "+ex.getMessage());
		}
		return false;
	}

	public static boolean IsReachable(String remoteInetAddr ,int port)
	{
		boolean isReachable = false;   
		Socket socket = null;   
		try{   
				socket = new Socket();   
				// �˿ں�����Ϊ 0 ��ʾ�ڱ�����ѡһ�����ö˿ڽ�������  
				 InetSocketAddress endpointSocketAddr =   
				 new InetSocketAddress(remoteInetAddr, port);   
				 socket.connect(endpointSocketAddr, 8000);           
				 isReachable = true;   
				}
				catch(IOException e) {   
				} 
				finally{   
					 if(socket != null) {   
					 try{   
						 socket.close();   
					 } 
					 catch(IOException e) {   
					    System.out.println("Error occurred while closing socket..");   
					 }   
				 }   
			}   
			return isReachable;   
	}
	
	public static boolean IsReachable(String remoteInetAddrPort)
	{
		int firstIndex= remoteInetAddrPort.indexOf(':');
		int secondIndex=remoteInetAddrPort.indexOf(":", firstIndex);
		if(secondIndex==-1)
			return IsReachable( remoteInetAddrPort,80 );
		String portStr=remoteInetAddrPort.substring(secondIndex).replace(':', ' ').trim();
		String ip=remoteInetAddrPort.substring(0, secondIndex);
		int port=Convert.toInt( portStr);
		return IsReachable(ip,port);
	}
	
	public static boolean TestConect(String remoteIP,int port)
	{
		try {
			URL url=new URL("http://"+remoteIP+":"+port);
			URLConnection conn=url.openConnection();
			HttpURLConnection httpConn=null;
		
			if(!(conn instanceof HttpURLConnection))
				//throw new IOException("Not an Http Connection");
				return false;
			
			httpConn=(HttpURLConnection)conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.setConnectTimeout(3500);
			httpConn.connect();
//			httpConn.getResponseCode();
			httpConn.disconnect();
			return true;
		} catch (Exception ex) {
			// TODO: handle exception
			Log.d("EnvAppDemo"," EnvAppDemo "+ ex.getMessage());
			//throw new IOException("Error Connecting ");
			return false;
		}
	}
	
	public static boolean GetNewworkState()
	{
		 // ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
        ConnectivityManager connectivityManager = (ConnectivityManager) SuncereApplication.CurrentApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // ��ȡNetworkInfo����
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===״̬===" + networkInfo[i].getState());
                    System.out.println(i + "===����===" + networkInfo[i].getTypeName());
                    // �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
	}
}
