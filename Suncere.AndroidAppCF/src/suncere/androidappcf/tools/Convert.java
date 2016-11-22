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
     *  如果s = null return ""， 否则返回Object的前10位字符，用于日期型 
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
     *  如果s = null return ""， 否则返回s 本身。 
     * 
     * @param s String 
     * @return String 
     */  
    public static String toString(String s) {  
        return (s == null) ? "" : s;  
    }  
  
    /** 
     * 如果s = null 返回 ""; 
     * 如果s的长度大于length，则返回 前length-3长度的字串并加上“...”; 
     * 否则返回 s 本身。 
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
     * 如果date ＝ null ，返回“”，否则返回date.toString(); 
     * @param date Date 
     * @return String 
     */  
    public static String toString(Date date) {  
        return (null == date) ? "" : date.toString();  
    }  
  
    /** 
     * 格式化为“yyyy-MM-dd”的字符串 
     * @param date Date 
     * @return String 
     */  
    public static String toShortDate(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * 格式化为“yyyy-MM-dd HH:mm:ss”的字符串 
     * 
     * @param date Date 
     * @return String 
     */  
    public static String toLongDate(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * 格式化为“yyyy-MM-dd HH:mm:ss”的字符串 
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
     * 格式化为“yyyy-MM-dd HH:mm”的字符串 
     * 
     * @param date Date 
     * @return String 
     */  
    public static String toDateMin(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * 格式化为“HH:mm”的字符串 
     * @param date Date 
     * @return String 
     */  
    public static String toHourMin(java.util.Date date) {  
        DateFormat df = new SimpleDateFormat("HH:mm");  
        return (null == date) ? "" : df.format(date);  
    }  
  
    /** 
     * 返回整数的字符串形式； 如 1 －>“1”； 
     * 如果 i ＝ －1 ，则转换为“”。 
     * @param i int 
     * @return String 
     */  
    public static String toString(int i) {  
        return (i == -1) ? "" : String.valueOf(i);  
    }  
  
    /** 
     * 返回long类型的字符串形式；如1L转换为“1”； 
     * 
     * @param l long 
     * @return String 
     */  
    public static String toString(long l) {  
        return String.valueOf(l);  
    }  
  
    /** 
     * 返回double类型的字符串形式；如1.32转换为“1.32”； 
     * 
     * @param d double 
     * @return String 
     */  
    public static String toString(double d) {  
        return String.valueOf(d);  
    }  
  
    /** 
     * 返回Object类型的字符串形式; 
     * 如果为null 则返回“”；否则返回object.toString(); 
     * @param object Object 
     */  
    public static String toString(Object object) {  
        return (null == object) ? "" : object.toString();  
    }  
  
    /** 
     * 
     * 如果object = null 返回 ""; 
     * <br>如果object.toString()的长度大于length， 
     * 则返回 前length-4长度的字串并加上“....”;<br> 
     * 否则返回 s 本身。 
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
     * 按字符串内容构造日期： 
     * <br>格式为 “yyyy-mm-dd hh:mm:ss.fffffffff”，“yyyy-mm-dd hh:mm:ss” 
     *      或“yyyy-mm-dd”有效。<br> 
     * <br>如果 s ＝null 或“”，则返回null；<br> 
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
     * 如果 s ＝null ，则返回null； 
     * 否则按s.toString()字符串内容构造日期： 
     * <br>格式为 “yyyy-mm-dd hh:mm:ss.fffffffff”，“yyyy-mm-dd hh:mm:ss” 
     *      或“yyyy-mm-dd”有效。 
     * 
     * @param s String 
     * @return Date 
     */  
    public static Date toDate(Object s) {  
        return toDate(toString(s));  
    }  
  
    /** 
     * 返回d 
     * @param d Date 
     * @return Date 
     */  
    public static Date toDate(Date d) {  
        return d;  
    }  
  
    /** 
     * 如果s ＝ null 返回 null 
     * 否则按s.toString()字符串内容构造日期,并设置日期的时分秒为“23：59：59”： 
     * <br>格式为 “yyyy-mm-dd hh:mm:ss.fffffffff”，“yyyy-mm-dd hh:mm:ss” 
     *      或“yyyy-mm-dd”有效。<br> 
     * 
     * @param s Object 
     * @return Date 
     */  
    public static Date toEndDate(Object s) {  
        return getDayEnd(toDate(toString(s)));  
    }  
  
    /** 
     * 如果s ＝ null 返回 null 
     * 否则设置日期的时分秒为“23：59：59”： 
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
     * 如果s ＝ null 返回 null 
     * 否则设置日期的时分秒为“0：0：0”： 
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
     * 如果s ＝ null或“”，返回 －1，否则返回字符串对应的整数；如“10”－－> 10 
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
     * 如果s ＝ null或“”，返回 null，否则返回字符串对应的Double； 
     * <br>ex. “10.0”－－> new Double(10.0)<br> 
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
     * 如果s ＝ null或“”，返回 －1.0，否则返回字符串对应的double数； 
     * <br>如“10.1”－－> 10.1 
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
     * 如果s ＝ null 返回 －1；否则返回s.toSting()对应的int 
     * @param s Object 
     * @return int 
     */  
    public static int toInt(Object s) {  
        return toInt(toString(s));  
    }  
  
    /** 
     * 如果s ＝ null或“” 返回 null；否则返回 s 对应的Integer 
     *<br> 如 “3”－－> new Integer(3) 
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
     * 如果s ＝ null 返回 null；否则返回 s.toString() 对应的Integer 
     * <br>如 “3”－－> new Integer(3)<br> 
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
     * 如果i＝ -1 返回 null；否则返回 i 对应的Integer 
     * 如 1－－> new Integer(1) 
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
     * 格式化 Calendar 为 "yyyyMMddHHmmss" 格式 
     * @param calValue Calendar 
     * @return String 
     */  
    public static String getStr(Calendar calValue) {  
        return getStr(calValue, "yyyyMMddHHmmss");  
    }  
  
    /** 
     *  格式化 Calendar 为 patten 格式 
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
     * 取得系统当前时间 
     * @return Date 
     */  
    public static Date getCurrentDate() {  
        Calendar calValue = Calendar.getInstance();  
        return new Date(calValue.getTime().getTime());  
    }  
  
    /** 
     * 取得当前日期years年后的日期 
     * @param years int 
     * @return Date 
     */  
    public static Date getAfterDateByYears(int years) {  
        Calendar calValue = Calendar.getInstance();  
        calValue.add(Calendar.YEAR, years);  
        return new Date(calValue.getTime().getTime());  
    }  
  
    /** 
     * 取得当前日期days天后的日期 
     * @param days int 
     * @return Date 
     */  
    public static Date getAfterDateByDays(int days) {  
        Calendar calValue = Calendar.getInstance();  
        calValue.add(Calendar.DATE, days);  
        return new Date(calValue.getTime().getTime());  
    }  
  
    /** 
     * 把number格式化为len长度的字串。 
     * 不足时前面加0补足；超长时取前面len长度的字串。 
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
     * 把number格式化为len长度的字串。 
     * 不足时前面加0补足；超长时取前面len长度的字串。 
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
     * 得到随机len长度的字串。 
     * @param len int 
     * @param number int 
     * @return String 
     */  
    public static String getRandomStr(int len) {  
        return formatStr(len, toString(Math.round(Math.pow(10  
            , len) * Math.random())));  
    }  
  
    /** 
     * 取得 year 年的第weeksOfYear周的第一天和最后一天的数组。 
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
     * 取得enddate 之间 startdate的秒数 
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
     * 取得当前年份 
     * @return int 
     */  
    public static int getCurrentYear() {  
        Calendar c = Calendar.getInstance();  
        return c.get(Calendar.YEAR);  
    }  
  
    /** 
     * 按照每组的数目对list进行分组 
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
     * 按照组数目对list进行分组 
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
     * 按分隔符分解字串 
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
        return s.append(totalM).append("分钟").toString();  
    }  
  
    /** 
         * 此方法将给出的字符串source使用delim划分为单词数组。 
         * 
         * @param source 
         *            需要进行划分的原字符串 
         * @param delim 
         *            单词的分隔字符串 
         * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组， 
         *         如果delim为null则使用逗号作为分隔字符串。 
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
         * 首字母变大写 
         * @param str 
         * @return 
         */  
        public static String toUpperFirstCase(String str){  
            char[] arg = str.toCharArray();  
            arg[0] = Character.toUpperCase(arg[0]);  
            return new String(arg);  
        }  
  
        /** 
         * 字符串替换 
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
         * 字符串替换，忽略大小写 
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
         * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符） 
         *  
         * @param char 
         *            c, 需要判断的字符 
         * @return boolean, 返回true,Ascill字符 
         */  
        public static boolean isLetter(char c) {  
            int k = 0x80;  
            return c / k == 0 ? true : false;  
        }  
  
        /** 
         * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1 
         *  
         * @param String 
         *            s ,需要得到长度的字符串 
         * @return int, 得到的字符串长度 
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
         * 截取含有汉字的字符串的部分字符串 
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
         * 禁止输出HTML代码 
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
         * dnyabean的结果对象 
         *  
         * @param object 
         * @return 默认返回“” 
         * @author XMJ 
         */  
        public static String getString(Object object) {  
            String result = ""; // 默认为空  
            if (object.toString().indexOf("java.lang.Object") == -1) {  
                result = object.toString();  
            }  
            return result;  
        }  
  
        /** 
         * dnyabean的结果对象,如果为空就输入指定默认为String def 描述： StrTool.java 时间： Nov 22, 2006 
         * 2:33:38 PM 作者： 吴绍叶 联系方式： wsyandy@126.com 
         *  
         * @param object 
         * @param def 
         *            指定默认String 
         * @return 返回类型： String 
         */  
        public static String getString(Object object, String def) {  
            String result = def; // 默认为def  
            if (object.toString().indexOf("java.lang.Object") == -1) {  
                result = object.toString();  
            }  
            return result;  
        }  
  
        /** 
         * 该方法的作用是处理DynaBean的返回数据， 
         *  
         * @param object 
         *            dnyabean的结果对象· 自动.toString()转型 
         * @param len 
         *            量度的长度 
         * @param num 
         *            截取字数 
         * @return 默认返回“” 
         * @author XMJ 
         */  
        public static String getString(Object object, int len) {  
            String result = ""; // 默认为空  
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
         * 该方法的作用是处理DynaBean的返回数据， 
         *  
         * @param object 
         *            dnyabean的结果对象· 自动.toString()转型 
         * @param len 
         *            量度的长度 
         * @param flag 
         *            超过字数部分用符号代替如：(...)省略号 
         * @return 默认返回“” 
         * @author wsy 
         */  
        public static String getString(Object object, int len, String flag) {  
            String result = ""; // 默认为""  
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
        /**中文乱码的处理函数，入参是有问题的字符串，出参是问题已经解决了的字符串 
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
            System.out.println("后3s:"+s);  
            return s;  
        }  
          
        /** 
         * 转换函数，16进制字符转换成普通字符 
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
            } else { // 对应的字符未经过编码   
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
            //中文文章缩略内容截取方法实现   
             /** 
             * 截取字符串的前targetCount个字符 
             * @param str 被处理字符串 
             * @param targetCount 截取长度 
             * @param more 后缀字符串 
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
             * 截取指定文章内容 
             * @param str 
             * @param n 
             * @author aithero 
             * @return String  
             */  
            public static String subContent(String str,int n)  
            {  
             //格式化字符串长度，超出部分显示省略号,区分汉字跟字母。汉字2个字节，字母数字一个字节  
              String temp= "";  
              if(str.length()<n){//如果长度比需要的长度n小,返回原字符串  
               return str;  
              }else{  
                            int t=0;  
                            char[] tempChar=str.toCharArray();  
                            for(int i=0;i<tempChar.length&&t<n;i++)  
                            {  
                                    if((int)tempChar[i]>=0x4E00 && (int)tempChar[i]<=0x9FA5)//是否汉字  
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
             * 获取缩略图路径 
             * @param prefix 
             * @param object 
             * @param def 
             * @return 
             */  
            public static String getZoompic(String prefix,Object object, String def) {  
                String result = def; // 默认为def  
                if (object.toString().indexOf("java.lang.Object") == -1) {  
                    result = prefix+object.toString();  
                }  
                return result;  
            }  
            public  static String delFckTextOfHtml(String Context)  
            {  String re="暂无内容";  
                 if((Context==null)||("".equals(Context)))  
                 {  
                     return re;  
                 }  
                try{      
                    re=Context.replaceAll("\"", "“");  
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
