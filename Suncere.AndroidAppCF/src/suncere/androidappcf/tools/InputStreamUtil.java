package suncere.androidappcf.tools;

import java.io.*;

public class InputStreamUtil {
	
	final static int BUFFER_SIZE = 4096;
	
	/**
	 * ��InputStreamת����String
	 * @param in InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String InputStream2String(InputStream in) throws Exception{
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while((count = in.read(data,0,BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		
		data = null;
		return new String(outStream.toByteArray());
//		return new String(outStream.toByteArray(),"ISO-8859-1");
	}
    
    /**
     * ��Stringת����InputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream String2InputStream(String in) throws Exception{
    	
    	ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
    	return is;
    }
    
    /**
     * ��InputStreamת����byte����
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStream2ByteArray(InputStream in) throws IOException{
    	
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while((count = in.read(data,0,BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		
		data = null;
		return outStream.toByteArray();
    }
    
    /**
     * ��byte����ת����InputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream ByteArray2InputStream(byte[] in) throws Exception{
    	
    	ByteArrayInputStream is = new ByteArrayInputStream(in);
    	return is;
    }
    
    /**
     * ��byte����ת����String
     * @param in
     * @return
     * @throws Exception
     */
    public static String ByteArray2String(byte[] in) throws Exception{
    	
    	InputStream is = ByteArray2InputStream(in);
    	return InputStream2String(is);
    }
}
