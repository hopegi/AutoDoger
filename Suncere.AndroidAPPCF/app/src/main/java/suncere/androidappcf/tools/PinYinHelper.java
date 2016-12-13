package suncere.androidappcf.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinHelper 
{
    /** 
     * �õ� ȫƴ 
     *  
     * @param src 
     * @return 
     */  
    public static String getPingYin(String src) {  
        char[] t1 = null;  
        t1 = src.toCharArray();  
        String[] t2 = new String[t1.length];  
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();  
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);  
        String t4 = "";  
        int t0 = t1.length;  
        try {  
            for (int i = 0; i < t0; i++) {  
                // �ж��Ƿ�Ϊ�����ַ�  
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {  
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
                    t4 += t2[0];  
                } else {  
                    t4 += Character.toString(t1[i]);
                }  
            }  
            return t4;  
        } catch (BadHanyuPinyinOutputFormatCombination e1) {  
            e1.printStackTrace();  
        }  
        return t4;  
    }  
  
    /** 
     * �õ���һ���ֵı�׼����ĸ�������� 
     *  
     * @param str 
     * @return 
     */  
    public static String getStandardHeadChar(String str) {  
  
        String convert = "";  
        char word = str.charAt(0);  
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
        if (pinyinArray != null) {  
            convert += AnalyzeHead(pinyinArray[0]);//pinyinArray[0].charAt(0);  
        } else {  
            convert += word;  
        }  
        return convert.toUpperCase();  
    }  
      
    /** 
     * �õ���������ĸ��д ��������
     *  
     * @param str 
     * @return 
     */  
    public static String getStandardPinYinHeadChar(String str) {  
  
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {  
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {  
                convert += AnalyzeHead(pinyinArray[0]);//pinyinArray[0].charAt(0);  
            } else {  
                convert += word;  
            }  
        }  
        return convert.toUpperCase();  
    }  
 
    /** 
     * �õ���һ���ֵ�����ĸ ����������
     *  
     * @param str 
     * @return 
     */  
    public static String getHeadChar(String str) {  
  
        String convert = "";  
        char word = str.charAt(0);  
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
        if (pinyinArray != null) {  
            convert += pinyinArray[0].charAt(0);  
        } else {  
            convert += word;  
        }  
        return convert.toUpperCase();  
    }  
      
    /** 
     * �õ���������ĸ��д ����������
     *  
     * @param str 
     * @return 
     */  
    public static String getPinYinHeadChar(String str) {  
  
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {  
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {  
                convert += pinyinArray[0].charAt(0);  
            } else {  
                convert += word;  
            }  
        }  
        return convert.toUpperCase();  
    }  

    
    //�����ó�
    protected static String AnalyzeHead(String pyStr)
    {
    	if(pyStr==null||pyStr.length()==0)return "";
    	if(pyStr.charAt(0)=='z'||pyStr.charAt(0)=='c'||pyStr.charAt(0)=='s')
    	{
    		return pyStr.charAt(1)=='h'?pyStr.charAt(0)+"h":String.valueOf(pyStr.charAt(0));
    	}
    	else
    		return 	String.valueOf( pyStr.charAt(0));
    }
}
