package suncere.androidapp.autobasemodule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParameterHelper {

	public final static String LIKE="like";
	public final static String IN="in";
	public final static String EQUAL="=";
	
	public final static String DbPrefix="@";
	public final static String NetPrefix="&";
	
	private final static String[] dbOperator=new String[]{">=","<=","<>",">","<",EQUAL,LIKE,IN};
	
	private static Pattern namePattern=Pattern.compile("((_[_a-zA-Z0-9]+)|([a-zA-Z][_a-zA-Z0-9]*))");
	
	public static String GetDbParemeterName(String key)
	{
		if(key.startsWith(NetPrefix))
		{
			return "";
		}
		return GetParameterNameInner(key);
	}
	
	private static String GetParameterNameInner(String key)
	{
		Matcher match=namePattern.matcher(key);
		while(match.find())
		{
			return match.group();
		}
		return "";
	}
	
	public static String GetNetParameterName(String key)
	{
		if(key.startsWith(DbPrefix))
		{
			return "";
		}
		return GetParameterNameInner(key);
	}
	
	public static String GetDbOperator(String key)
	{
		for(String op : dbOperator)
		{
			if(key.contains(op))
			{
				return op;
			}
		}
		return EQUAL;
	}
}
