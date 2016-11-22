package suncere.androidappcf.tools;

import java.io.UnsupportedEncodingException;  
import java.sql.Date;  
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Enumeration;  
import java.util.StringTokenizer;  
import java.util.Vector;  


public class Convert 
{
	/** 
     *  ���s = null return ""�� ���򷵻�Object��ǰ10λ�ַ������������� 
     * 
     * @param s String 
     * @return String 
     */  
    public static String timesTampString(Object s) {  
        return (s == null || s.equals("")) ? "" : s.toString().substring(0,10);  
    }  
    public static String timesTampToString(Object s) {  
        return (s == null || s.equals("")) ? "" : s.toString().substring(0,19);  
    }  
    /** 
     *  ���s = null return ""�� ���򷵻�s ���� 
     * 
     * @param s String 
     * @return String 
     */  
    public static String toString(String s) {  
        return (s == null) ? "" : s;  
    }  
  
    /** 
     * ���s = null ���� ""; 
     * ���s�ĳ��ȴ���length���򷵻� ǰlength-3���ȵ��ִ������ϡ�...��; 
     * ���򷵻� s ���� 
     * @param s String 
     * @param length int 
     * @return String 
     */  
    public static String trimString(String s, int length) {  
        if (s == null) {  
            return "";  
        } else if (s.length() > length) {  
            return s.substring(0, length - 3) + "...";  
        } else {  
            return s;  
        }  
    }  
  
    /** 
     * ���date �� null �����ء��������򷵻�date.toString(); 
     * @param date Date 
     * @return String 
     */  
    public static String toString(Date date) {  
        return (null == date) ? "" : date.toString();  
    }  
  
    /** 
     * ��ʽ��Ϊ��yyyy-MM-dd�����ַ��� 
     * @param date Date 
     * @return String 
     */  
    public static String toShortDate(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * ��ʽ��Ϊ��yyyy-MM-dd HH:mm:ss�����ַ��� 
     * 
     * @param date Date 
     * @return String 
     */  
    public static String toLongDate(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * ��ʽ��Ϊ��yyyy-MM-dd HH:mm:ss�����ַ��� 
     * 
     * @param date Date 
     * @return String 
     */  
    public static String toLongDate(String date) {  
        if (date!=null)  
            return date.substring(0,19);  
        else  
            return "";  
    }  
  
    /** 
     * ��ʽ��Ϊ��yyyy-MM-dd HH:mm�����ַ��� 
     * 
     * @param date Date 
     * @return String 
     */  
    public static String toDateMin(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * ��ʽ��Ϊ��HH:mm�����ַ��� 
     * @param date Date 
     * @return String 
     */  
    public static String toHourMin(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("HH:mm");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * �����������ַ�����ʽ�� �� 1 ��>��1���� 
     * ��� i �� ��1 ����ת��Ϊ������ 
     * @param i int 
     * @return String 
     */  
    public static String toString(int i) {  
        return (i == -1) ? "" : String.valueOf(i);  
    }  
  
    /** 
     * ����long���͵��ַ�����ʽ����1Lת��Ϊ��1���� 
     * 
     * @param l long 
     * @return String 
     */  
    public static String toString(long l) {  
        return String.valueOf(l);  
    }  
  
    /** 
     * ����double���͵��ַ�����ʽ����1.32ת��Ϊ��1.32���� 
     * 
     * @param d double 
     * @return String 
     */  
    public static String toString(double d) {  
        return String.valueOf(d);  
    }  
  
    /** 
     * ����Object���͵��ַ�����ʽ; 
     * ���Ϊnull �򷵻ء��������򷵻�object.toString(); 
     * @param object Object 
     */  
    public static String toString(Object object) {  
        return (null == object) ? "" : object.toString();  
    }  
  
    /** 
     * 
     * ���object = null ���� ""; 
     * <br>���object.toString()�ĳ��ȴ���length�� 
     * �򷵻� ǰlength-4���ȵ��ִ������ϡ�....��;<br> 
     * ���򷵻� s ���� 
     * @param object Object 
     * @param length int 
     * @return String 
     */  
    public static String toString(Object object, int length) {  
        if (object == null) {  
            return "";  
        } else if (object.toString().trim().length() > length) {  
            return object.toString().trim().substring(0, length - 4) + "....";  
        } else {  
            return object.toString().trim();  
        }  
  
    }  
  
    /** 
     * ���ַ������ݹ������ڣ� 
     * <br>��ʽΪ ��yyyy-mm-dd hh:mm:ss.fffffffff������yyyy-mm-dd hh:mm:ss�� 
     *      ��yyyy-mm-dd����Ч��<br> 
     * <br>��� s ��null �򡰡����򷵻�null��<br> 
     * 
     * @param s String 
     * @return Date 
     */  
    public static Date toDate(String s) {  
        if (null == s || "".equals(s)) {  
            return null;  
        } else {  
            if (s.indexOf(":") < 0) {  
                return Date.valueOf(s);  
            } else if (s.indexOf(":") != s.lastIndexOf(":")) {  
                return new Date(java.sql.Timestamp.valueOf(s).getTime());  
            } else {  
                return new Date(java.sql.Timestamp.valueOf(s.concat(":0")).  
                                getTime());  
            }  
        }  
    }  
  
    /** 
     * 
     * ��� s ��null ���򷵻�null�� 
     * ����s.toString()�ַ������ݹ������ڣ� 
     * <br>��ʽΪ ��yyyy-mm-dd hh:mm:ss.fffffffff������yyyy-mm-dd hh:mm:ss�� 
     *      ��yyyy-mm-dd����Ч�� 
     * 
     * @param s String 
     * @return Date 
     */  
    public static Date toDate(Object s) {  
        return toDate(toString(s));  
    }  
  
    /** 
     * ����d 
     * @param d Date 
     * @return Date 
     */  
    public static Date toDate(Date d) {  
        return d;  
    }  
  
    /** 
     * ���s �� null ���� null 
     * ����s.toString()�ַ������ݹ�������,���������ڵ�ʱ����Ϊ��23��59��59���� 
     * <br>��ʽΪ ��yyyy-mm-dd hh:mm:ss.fffffffff������yyyy-mm-dd hh:mm:ss�� 
     *      ��yyyy-mm-dd����Ч��<br> 
     * 
     * @param s Object 
     * @return Date 
     */  
    public static Date toEndDate(Object s) {  
        return getDayEnd(toDate(toString(s)));  
    }  
  
    /** 
     * ���s �� null ���� null 
     * �����������ڵ�ʱ����Ϊ��23��59��59���� 
     * @param s Object 
     * @return Date 
     */  
    public static Date getDayEnd(Date d) {  
        if (d == null) {  
            return null;  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(d);  
        c.set(Calendar.HOUR_OF_DAY, 23);  
        c.set(Calendar.MINUTE, 59);  
        c.set(Calendar.SECOND, 59);  
        return new Date(c.getTime().getTime());  
    }  
  
    /** 
     * ���s �� null ���� null 
     * �����������ڵ�ʱ����Ϊ��0��0��0���� 
     * @param d Date 
     * @return Date 
     */  
    public static Date getDayStart(Date d) {  
        if (d == null) {  
            return null;  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(d);  
        c.set(Calendar.HOUR_OF_DAY, 0);  
        c.set(Calendar.MINUTE, 0);  
        c.set(Calendar.SECOND, 0);  
       return new Date(c.getTime().getTime());  
  
    }  
  
    /** 
     * ���s �� null�򡰡������� ��1�����򷵻��ַ�����Ӧ���������硰10������> 10 
     * @param s String 
     * @return int 
     */  
    public static int toInt(String s) {  
        try {  
          return (null == s || "".equals(s)) ? -1 : Integer.valueOf(s).intValue();  
        }  
        catch (NumberFormatException ex) {  
          return -1;  
        }  
    }  
  
    /** 
     * ���s �� null�򡰡������� null�����򷵻��ַ�����Ӧ��Double�� 
     * <br>ex. ��10.0������> new Double(10.0)<br> 
     * @param s String 
     * @return Double 
     */  
    public static Double toDouble(String s) {  
        return (null == s || "".equals(s)) ? null : Double.valueOf(s);  
    }  
  
    public static Long toLong(String s) {  
        return (null == s || "".equals(s)) ? null : Long.valueOf(s);  
    }  
  
    /** 
     * ���s �� null�򡰡������� ��1.0�����򷵻��ַ�����Ӧ��double���� 
     * <br>�硰10.1������> 10.1 
     * @param s String 
     * @return int 
     */  
    public static double todouble(String s) {  
        return (null == s || "".equals(s)) ? -1 : Double.valueOf(s).doubleValue();  
    }  
  
    public static long tolong(String s) {  
        return (null == s || "".equals(s)) ? -1 : Long.valueOf(s).longValue();  
    }  
  
    /** 
     * ���s �� null ���� ��1�����򷵻�s.toSting()��Ӧ��int 
     * @param s Object 
     * @return int 
     */  
    public static int toInt(Object s) {  
        return toInt(toString(s));  
    }  
  
    /** 
     * ���s �� null�򡰡� ���� null�����򷵻� s ��Ӧ��Integer 
     *<br> �� ��3������> new Integer(3) 
     * @param string String 
     * @return Object 
     */  
    public static Integer toInteger(String s) {  
        if (s == null || "".equals(s) || s.equals("-1")  
            || "null".equalsIgnoreCase(s)) {  
            return null;  
        } else {  
            return Integer.valueOf(s);  
        }  
    }  
  
    /** 
     * ���s �� null ���� null�����򷵻� s.toString() ��Ӧ��Integer 
     * <br>�� ��3������> new Integer(3)<br> 
     * @param string String 
     * @return Object 
     */  
    public static Integer toInteger(Object o) {  
        if (o == null) {  
            return null;  
        } else {  
            return Integer.valueOf(o.toString());  
        }  
    }  
  
    /** 
     * ���i�� -1 ���� null�����򷵻� i ��Ӧ��Integer 
     * �� 1����> new Integer(1) 
     * @param i int 
     * @return Object 
     */  
    public static Object toInteger(int i) {  
        return (i == -1) ? null : new Integer(i);  
    }  
  
    /** 
     * return null; 
     * @return String 
     */  
    public static String getNull() {  
        return null;  
    }  
  
    /** 
     * ��ʽ�� Calendar Ϊ "yyyyMMddHHmmss" ��ʽ 
     * @param calValue Calendar 
     * @return String 
     */  
    public static String getStr(Calendar calValue) {  
        return getStr(calValue, "yyyyMMddHHmmss");  
    }  
  
    /** 
     *  ��ʽ�� Calendar Ϊ patten ��ʽ 
     * @param calValue Calendar 
     * @param patten String 
     * @return String 
     */  
    public static String getStr(Calendar calValue, String patten) {  
        SimpleDateFormat formatter = new SimpleDateFormat(patten);  
        if (calValue == null) {  
            return "";  
        } else {  
            return formatter.format(calValue.getTime());  
        }  
    }  
  
    /** 
     * ȡ��ϵͳ��ǰʱ�� 
     * @return Date 
     */  
    public static Date getCurrentDate() {  
        Calendar calValue = Calendar.getInstance();  
        return new Date(calValue.getTime().getTime());  
    }  
  
    /** 
     * ȡ�õ�ǰ����years�������� 
     * @param years int 
     * @return Date 
     */  
    public static Date getAfterDateByYears(int years) {  
        Calendar calValue = Calendar.getInstance();  
        calValue.add(Calendar.YEAR, years);  
        return new Date(calValue.getTime().getTime());  
    }  
  
    /** 
     * ȡ�õ�ǰ����days�������� 
     * @param days int 
     * @return Date 
     */  
    public static Date getAfterDateByDays(int days) {  
        Calendar calValue = Calendar.getInstance();  
        calValue.add(Calendar.DATE, days);  
        return new Date(calValue.getTime().getTime());  
    }  
  
    /** 
     * ��number��ʽ��Ϊlen���ȵ��ִ��� 
     * ����ʱǰ���0���㣻����ʱȡǰ��len���ȵ��ִ��� 
     * @param len int 
     * @param number int 
     * @return String 
     */  
    public static String formatStr(int len, int number) {  
        String num = String.valueOf(number);  
        String space30 = "000000000000000000000000000000";  
        if (num.length() < len) {  
            return space30.substring(30 - len + num.length()) + num;  
        } else {  
            return num.substring(num.length() - len);  
        }  
    }  
  
    /** 
     * ��number��ʽ��Ϊlen���ȵ��ִ��� 
     * ����ʱǰ���0���㣻����ʱȡǰ��len���ȵ��ִ��� 
     * @param len int 
     * @param number int 
     * @return String 
     */  
    public static String formatStr(int len, String num) {  
        String space30 = "000000000000000000000000000000";  
        if (num.length() < len) {  
            return space30.substring(30 - len + num.length()) + num;  
        } else {  
            return num.substring(num.length() - len);  
        }  
    }  
  
    /** 
     * �õ����len���ȵ��ִ��� 
     * @param len int 
     * @param number int 
     * @return String 
     */  
    public static String getRandomStr(int len) {  
        return formatStr(len, toString(Math.round(Math.pow(10  
            , len) * Math.random())));  
    }  
  
    /** 
     * ȡ�� year ��ĵ�weeksOfYear�ܵĵ�һ������һ������顣 
     * 
     * @param i int 
     * @return Date[] 
     */  
    public static Date[] getBetweenDate(int year, int weeksOfYear) {  
        Calendar c = Calendar.getInstance();  
  
        if (year <= 0) {  
            year = c.get(Calendar.YEAR);  
        }  
        if (weeksOfYear <= 0) {  
            weeksOfYear = c.get(Calendar.WEEK_OF_YEAR) - 1;  
        }  
        c.set(Calendar.YEAR, year);  
        Date[] betweens = new Date[2];  
  
        c.set(Calendar.DAY_OF_WEEK, 1);  
        c.set(Calendar.WEEK_OF_YEAR, weeksOfYear);  
        betweens[0] = toDate(new Date(c.getTime().getTime()).toString());  
  
        c.set(Calendar.DAY_OF_WEEK, 7);  
        c.set(Calendar.WEEK_OF_YEAR, weeksOfYear);  
        betweens[1] = toEndDate(new Date(c.getTime().getTime()).toString());  
  
        return betweens;  
    }  
  
    /** 
     * ȡ��enddate ֮�� startdate������ 
     * @param startdate Date 
     * @param enddate Date 
     * @return int 
     */  
    public static int getSeconds(Date startdate, Date enddate) {  
        long time = enddate.getTime() - startdate.getTime();  
        int totalS = new Long(time / 1000).intValue();  
        return totalS;  
    }  
  
    /** 
     * ȡ�õ�ǰ��� 
     * @return int 
     */  
    public static int getCurrentYear() {  
        Calendar c = Calendar.getInstance();  
        return c.get(Calendar.YEAR);  
    }  
  
    /** 
     * ����ÿ�����Ŀ��list���з��� 
     * @param list Vector 
     * @param itemPerGroup int 
     * @return Vector 
     */  
    public static Vector groupByItems(Vector list, int itemPerGroup) {  
        int listLen = list.size();  
        if (itemPerGroup >= listLen) {  
            itemPerGroup = listLen;  
        }  
        if (itemPerGroup <= 0) {  
            itemPerGroup = 1;  
        }  
        int groupCounts = listLen / itemPerGroup;  
        if (listLen % itemPerGroup > 0) {  
            groupCounts++;  
        }  
  
        return group(list, itemPerGroup, groupCounts);  
    }  
  
    private static Vector group(Vector list, int itemPerGroup, int groups) {  
        Vector newVector = new Vector();  
        Vector childV = new Vector();  
        for (int j = 0; j < groups; j += itemPerGroup) {  
            for (int i = 0; i < itemPerGroup; i++) {  
                int count = i * (j + 1);  
                if (count >= list.size()) {  
                    break;  
                }  
                childV.add(i, list.elementAt(count));  
            }  
            newVector.add(childV);  
            childV = new Vector();  
        }  
  
        return newVector;  
    }  
  
    /** 
     * ��������Ŀ��list���з��� 
     * @param list Vector 
     * @param groupCounts int 
     * @return Vector 
     */  
    public static Vector groupByGroups(Vector list, int groupCounts) {  
        int listLen = list.size();  
        if (groupCounts >= listLen) {  
            groupCounts = listLen;  
        }  
        if (groupCounts <= 0) {  
            groupCounts = 1;  
        }  
        int itemPerGroup = listLen / groupCounts;  
        if (listLen % groupCounts > 0) {  
            itemPerGroup++;  
        }  
  
        return group(list, itemPerGroup, groupCounts);  
    }  
  
  
    /** 
     * ���ָ����ֽ��ִ� 
     * @param s String 
     * @param delim String 
     * @return Enumeration 
     */  
    public static Enumeration splitString(String s, String delim) {  
        Vector vTokens = new Vector();  
        int currpos = 0;  
        for (int delimpos = s.indexOf(delim, currpos); delimpos != -1;  
             delimpos = s.indexOf(delim, currpos)) {  
            String ss = s.substring(currpos, delimpos);  
            vTokens.addElement(ss);  
            currpos = delimpos + delim.length();  
        }  
  
        vTokens.addElement(s.substring(currpos));  
        return vTokens.elements();  
    }  
  
    public static String paddingToEight(String s) {  
        final String space8 = "        ";  
  
        int len = s.getBytes().length % 8;  
        if (len == 0) {  
            return s;  
        }  
        else {  
            return s + space8.substring(len);  
        }  
  
    }  
  
    public static String getTime(Date startdate, Date enddate) {  
        long time = enddate.getTime() - startdate.getTime();  
        int totalM = new Long(time / (1000 * 60)).intValue();  
//        int minute = totalM % 60;  
//        int hour = totalM / 60;  
        StringBuffer s = new StringBuffer();  
        return s.append(totalM).append("����").toString();  
    }  
  
    /** 
         * �˷������������ַ���sourceʹ��delim����Ϊ�������顣 
         * 
         * @param source 
         *            ��Ҫ���л��ֵ�ԭ�ַ��� 
         * @param delim 
         *            ���ʵķָ��ַ��� 
         * @return �����Ժ�����飬���sourceΪnull��ʱ�򷵻���sourceΪΨһԪ�ص����飬 
         *         ���delimΪnull��ʹ�ö�����Ϊ�ָ��ַ����� 
         */  
        public static String[] split(String source, String delim) {  
                String[] wordLists;  
                if (source == null) {  
                        wordLists = new String[1];  
                        wordLists[0] = source;  
                        return wordLists;  
                }  
                if (delim == null) {  
                        delim = ",";  
                }  
                StringTokenizer st = new StringTokenizer(source, delim);  
                int total = st.countTokens();  
                wordLists = new String[total];  
                for (int i = 0; i < total; i++) {  
                        wordLists[i] = st.nextToken();  
                }  
                return wordLists;  
        }  
          
  
          
        /** 
         * ����ĸ���д 
         * @param str 
         * @return 
         */  
        public static String toUpperFirstCase(String str){  
            char[] arg = str.toCharArray();  
            arg[0] = Character.toUpperCase(arg[0]);  
            return new String(arg);  
        }  
  
        /** 
         * �ַ����滻 
         * @param line 
         * @param oldString 
         * @param newString 
         * @return 
         */  
        public static String replace(String line, String oldString, String newString) {  
            if (line == null) {  
                return null;  
            }  
            int i = 0;  
            if ((i = line.indexOf(oldString, i)) >= 0) {  
                char[] line2 = line.toCharArray();  
                char[] newString2 = newString.toCharArray();  
                int oLength = oldString.length();  
                StringBuffer buf = new StringBuffer(line2.length);  
                buf.append(line2, 0, i).append(newString2);  
                i += oLength;  
                int j = i;  
                while ((i = line.indexOf(oldString, i)) > 0) {  
                    buf.append(line2, j, i - j).append(newString2);  
                    i += oLength;  
                    j = i;  
                }  
                buf.append(line2, j, line2.length - j);  
                return buf.toString();  
            }  
            return line;  
        }  
  
        /** 
         * �ַ����滻�����Դ�Сд 
         * @param line 
         * @param oldString 
         * @param newString 
         * @return 
         */  
        public static String replaceIgnoreCase(String line, String oldString,  
                String newString) {  
            if (line == null) {  
                return null;  
            }  
            String lcLine = line.toLowerCase();  
            String lcOldString = oldString.toLowerCase();  
            int i = 0;  
            if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {  
                char[] line2 = line.toCharArray();  
                char[] newString2 = newString.toCharArray();  
                int oLength = oldString.length();  
                StringBuffer buf = new StringBuffer(line2.length);  
                buf.append(line2, 0, i).append(newString2);  
                i += oLength;  
                int j = i;  
                while ((i = lcLine.indexOf(lcOldString, i)) > 0) {  
                    buf.append(line2, j, i - j).append(newString2);  
                    i += oLength;  
                    j = i;  
                }  
                buf.append(line2, j, line2.length - j);  
                return buf.toString();  
            }  
            return line;  
        }  
  
        /** 
         * �ж�һ���ַ���Ascill�ַ����������ַ����纺���գ������ַ��� 
         *  
         * @param char 
         *            c, ��Ҫ�жϵ��ַ� 
         * @return boolean, ����true,Ascill�ַ� 
         */  
        public static boolean isLetter(char c) {  
            int k = 0x80;  
            return c / k == 0 ? true : false;  
        }  
  
        /** 
         * �õ�һ���ַ����ĳ���,��ʾ�ĳ���,һ�����ֻ��պ��ĳ���Ϊ2,Ӣ���ַ�����Ϊ1 
         *  
         * @param String 
         *            s ,��Ҫ�õ����ȵ��ַ��� 
         * @return int, �õ����ַ������� 
         */  
        public static int getLength(String s) {  
            char[] c = s.toCharArray();  
            int len = 0;  
            for (int i = 0; i < c.length; i++) {  
                len++;  
                if (!isLetter(c[i])) {  
                    len++;  
                }  
            }  
            return len;  
        }  
  
        /** 
         * ��ȡ���к��ֵ��ַ����Ĳ����ַ��� 
         * @param s 
         * @param slen 
         * @return 
         */  
        public static String gbSubstr(String s, int slen) {  
            if (slen < 1)  
                slen = 1;  
            if (slen > getLength(s))  
                slen = getLength(s);  
            if (s.length() == getLength(s)) {  
                return s.substring(0, slen - 1);  
            } else {  
                int gbNum = 0;  
                for (int i = 0; i < s.length(); i++) {  
                    if (!isLetter(s.charAt(i))) {  
                        gbNum++;  
                    }  
                    if (gbNum + i == slen)  
                        return s.substring(0, i + 1);  
                }  
                return s.substring(0, slen);  
            }  
        }  
  
        /** 
         * ��ֹ���HTML���� 
         * @param input 
         * @return 
         */  
        public static String escapeHTMLTags(String input) {  
            // Check if the string is null or zero length -- if so, return  
            // what was sent in.  
            if (input == null || input.length() == 0) {  
                return input;  
            }  
            // Use a StringBuffer in lieu of String concatenation -- it is  
            // much more efficient this way.  
            StringBuffer buf = new StringBuffer(input.length());  
            char ch = ' ';  
            for (int i = 0; i < input.length(); i++) {  
                ch = input.charAt(i);  
                switch (ch) {  
                case '<':  
                    buf.append("&lt;");  
                    break;  
                case '>':  
                    buf.append("&gt;");  
                    break;  
                case '\n':  
                    buf.append("<br>\n");  
                    break;  
                case '"':  
                    buf.append("&quot;");  
                    break;  
                default:  
                    buf.append(ch);  
                    break;  
                }  
  
            }  
            return buf.toString();  
        }  
  
        /** 
         *  
         * @param sOri 
         * @param sDefault 
         * @return 
         */  
        public static String getDefaultStr(String sOri, String sDefault) {  
            if (sOri == null)  
                sOri = sDefault;  
            return sOri;  
        }  
  
        /** 
         * dnyabean�Ľ������ 
         *  
         * @param object 
         * @return Ĭ�Ϸ��ء��� 
         * @author XMJ 
         */  
        public static String getString(Object object) {  
            String result = ""; // Ĭ��Ϊ��  
            if (object.toString().indexOf("java.lang.Object") == -1) {  
                result = object.toString();  
            }  
            return result;  
        }  
  
        /** 
         * dnyabean�Ľ������,���Ϊ�վ�����ָ��Ĭ��ΪString def ������ StrTool.java ʱ�䣺 Nov 22, 2006 
         * 2:33:38 PM ���ߣ� ����Ҷ ��ϵ��ʽ�� wsyandy@126.com 
         *  
         * @param object 
         * @param def 
         *            ָ��Ĭ��String 
         * @return �������ͣ� String 
         */  
        public static String getString(Object object, String def) {  
            String result = def; // Ĭ��Ϊdef  
            if (object.toString().indexOf("java.lang.Object") == -1) {  
                result = object.toString();  
            }  
            return result;  
        }  
  
        /** 
         * �÷����������Ǵ���DynaBean�ķ������ݣ� 
         *  
         * @param object 
         *            dnyabean�Ľ������ �Զ�.toString()ת�� 
         * @param len 
         *            ���ȵĳ��� 
         * @param num 
         *            ��ȡ���� 
         * @return Ĭ�Ϸ��ء��� 
         * @author XMJ 
         */  
        public static String getString(Object object, int len) {  
            String result = ""; // Ĭ��Ϊ��  
            if (object.toString().indexOf("java.lang.Object") == -1) {  
                if (object.toString().length() > len) {  
                    result = object.toString().substring(0, len);  
                } else {  
                    result = object.toString();  
                }  
            }  
            return result;  
        }  
  
        /** 
         * �÷����������Ǵ���DynaBean�ķ������ݣ� 
         *  
         * @param object 
         *            dnyabean�Ľ������ �Զ�.toString()ת�� 
         * @param len 
         *            ���ȵĳ��� 
         * @param flag 
         *            �������������÷��Ŵ����磺(...)ʡ�Ժ� 
         * @return Ĭ�Ϸ��ء��� 
         * @author wsy 
         */  
        public static String getString(Object object, int len, String flag) {  
            String result = ""; // Ĭ��Ϊ""  
            if (flag == null || "".equals(flag))  
                flag = "";  
            if (object.toString().indexOf("java.lang.Object") == -1) {  
                if (object.toString().length() <= len) {  
                    result = object.toString();  
  
                } else if (object.toString().length() > len) {  
                    result = object.toString().substring(0, len - 1) + flag;  
                }  
            }  
            return result;  
        }  
        /**��������Ĵ��������������������ַ����������������Ѿ�����˵��ַ��� 
         * @param in 
         * @return 
         * @author wsy 
         */  
        public static String parseGB(String in)  
        {  
            String s = null;  
            System.out.println("in3:"+in);  
            if (in == null)  
            {  
            System.out.println("Warn:Chinese null founded!");  
            return new String("");  
            }  
            try  
            {  
            s = new String(in.getBytes("ISO-8859-1"),"GBK");  
            }  
            catch(UnsupportedEncodingException e)  
            {  
            System.out.println (e.toString());  
            }  
            System.out.println("��3s:"+s);  
            return s;  
        }  
          
        /** 
         * ת��������16�����ַ�ת������ͨ�ַ� 
         * @param s 
         * @return 
         */  
        public static String unescape(String s) {   
            StringBuffer sbuf = new StringBuffer();   
            int i = 0;   
            int len = s.length();   
            while (i < len) {   
            int ch = s.charAt(i);   
            if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was   
            sbuf.append((char) ch);   
            } else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was   
            sbuf.append((char) ch);   
            } else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was   
            sbuf.append((char) ch);   
            } else if (ch == '-' || ch == '_' // unreserved : as it was   
            || ch == '.' || ch == '!' || ch == '~' || ch == '*'   
            || ch == '\'' || ch == '(' || ch == ')') {   
            sbuf.append((char) ch);   
            } else if (ch == '%') {   
            int cint = 0;   
            if ('u' != s.charAt(i + 1)) { // %XX : map to ascii(XX)   
            cint = (cint << 4) | val[s.charAt(i + 1)];   
            cint = (cint << 4) | val[s.charAt(i + 2)];   
            i += 2;   
            } else { // %uXXXX : map to unicode(XXXX)   
            cint = (cint << 4) | val[s.charAt(i + 2)];   
            cint = (cint << 4) | val[s.charAt(i + 3)];   
            cint = (cint << 4) | val[s.charAt(i + 4)];   
            cint = (cint << 4) | val[s.charAt(i + 5)];   
            i += 5;   
            }   
            sbuf.append((char) cint);   
            } else { // ��Ӧ���ַ�δ��������   
            sbuf.append((char) ch);   
            }   
            i++;   
            }   
            return sbuf.toString();   
            }   
            private final static byte[] val = {   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,   
            0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F   
            };  
            //���������������ݽ�ȡ����ʵ��   
             /** 
             * ��ȡ�ַ�����ǰtargetCount���ַ� 
             * @param str �������ַ��� 
             * @param targetCount ��ȡ���� 
             * @param more ��׺�ַ��� 
             * @version 0.1 
             * @author aithero 
             * @return String 
             */  
            public static String subContentString(String str, int targetCount,String more)  
            {  
              int initVariable = 0;  
              String restr = "";  
              if (str == null){  
                return "";  
              }else if(str.length()<=targetCount){  
               return str;  
              }else{  
             char[] tempchar = str.toCharArray();  
             for (int i = 0; (i < tempchar.length && targetCount > initVariable); i++) {  
              String s1 = str.valueOf(tempchar[i]);  
                 byte[] b = s1.getBytes();  
                 initVariable += b.length;  
                 restr += tempchar[i];  
             }  
              }  
              if (targetCount == initVariable || (targetCount == initVariable - 1)){  
                restr += more;  
              }  
              return restr;  
            }  
              
            /** 
             * ��ȡָ���������� 
             * @param str 
             * @param n 
             * @author aithero 
             * @return String  
             */  
            public static String subContent(String str,int n)  
            {  
             //��ʽ���ַ������ȣ�����������ʾʡ�Ժ�,���ֺ��ָ���ĸ������2���ֽڣ���ĸ����һ���ֽ�  
              String temp= "";  
              if(str.length()<n){//������ȱ���Ҫ�ĳ���nС,����ԭ�ַ���  
               return str;  
              }else{  
                            int t=0;  
                            char[] tempChar=str.toCharArray();  
                            for(int i=0;i<tempChar.length&&t<n;i++)  
                            {  
                                    if((int)tempChar[i]>=0x4E00 && (int)tempChar[i]<=0x9FA5)//�Ƿ���  
                                    {  
                                            temp+=tempChar[i];  
                                            t+=2;  
                                    }  
                                    else  
                                    {  
                                            temp+=tempChar[i];  
                                            t++;  
                                    }  
                            }  
                            return (temp+"...");  
                    }  
            }   
  
              
            /** 
             * ��ȡ����ͼ·�� 
             * @param prefix 
             * @param object 
             * @param def 
             * @return 
             */  
            public static String getZoompic(String prefix,Object object, String def) {  
                String result = def; // Ĭ��Ϊdef  
                if (object.toString().indexOf("java.lang.Object") == -1) {  
                    result = prefix+object.toString();  
                }  
                return result;  
            }  
            public  static String delFckTextOfHtml(String Context)  
            {  String re="��������";  
                 if((Context==null)||("".equals(Context)))  
                 {  
                     return re;  
                 }  
                try{      
                    re=Context.replaceAll("\"", "��");  
                    re= re.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(     
                            "<[^>]*>", "");     
                    re = re.replaceAll("[(/>)<]", "");  
                    System.out.println(re);  
                }catch(Exception ex)  
                {  
                    ex.printStackTrace();  
                }  
                  
                return re;  
            }  
	
	
}
