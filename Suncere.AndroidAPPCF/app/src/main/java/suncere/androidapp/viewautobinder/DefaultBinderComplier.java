package suncere.androidapp.viewautobinder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import cn.jpush.android.data.s;
import android.util.Log;
import suncere.androidappcf.tools.TypeHelper;

public class DefaultBinderComplier extends BaseBinderComplier{

	private HashMap<String,Object> datasourceCollection;
	private HashMap<String,String> defineCollection;
	
    private String NumberReg = "\\d+(\\.\\d+)?";
    private String StringReg = "'[^']*?'";
    private String FieldReg = "((_[_a-zA-Z0-9]+)|([a-zA-Z][_a-zA-Z0-9]*))";
    private String DefineFieldReg="#((_[_a-zA-Z0-9]+)|([a-zA-Z][_a-zA-Z0-9]*))#";

    private String ArrayReg = "\\[\\d+\\]";
    private String ArrayFieldIndexReg="\\[((_[_a-zA-Z0-9]+)|([a-zA-Z][_a-zA-Z0-9]*))\\]";
    private String MemberReg = "\\.";
    private String StrPlusReg = "\\+";
	
    private static HashMap<Object,List<Expression>> expressionCache=new HashMap<Object,List<Expression>>();
    private static Object expFlag=new Object();
    
	@Override
	public void InitComplier(HashMap<String, Object> parameters) {
		this.datasourceCollection=parameters;
	}
	
	//预处理参数
	public void SetPretreatmentParameter(HashMap<String,String> parameters)
	{
		this.defineCollection=parameters;
	}

	//需要加Logcat
	@Override
	public Object ComplieValue(Object statement) {

		//获取所有表达式实例
		 List<Expression> statementLst=null;
		synchronized (expFlag) {
			if(expressionCache.containsKey(statement))
			{
				statementLst=expressionCache.get(statement);
			}
		}
		if(statementLst==null)
		{
			statementLst= this.GetExpression(statement.toString());
			synchronized (expFlag) {
				expressionCache.put(statement, statementLst);
			}	
		}
		///深复制
		List<Expression> tmp=new ArrayList<Expression>( Arrays.asList(new Expression[ statementLst.size() ]));
		Collections.copy(tmp, statementLst);
		statementLst=tmp;
        
        ///预处理
        for(int i=0;i<statementLst.size();i++)
        {
        	if(statementLst.get(i) instanceof DefineFieldExpression)
        	{
        		DefineFieldExpression cur=(DefineFieldExpression) statementLst.get(i);
        		FieldExpression newExp=new FieldExpression();
        		newExp.StatementText=cur.GetValue();
        		statementLst.add(i, newExp);
        		statementLst.remove(cur);
        	}
        }
        
        //提取所有运算符并以优先级排序
        List<OpearterExpression> opeartionLst =new ArrayList<OpearterExpression>();
        for(Expression exp : statementLst)
        	if(exp instanceof OpearterExpression)
        		opeartionLst.add((OpearterExpression) exp);
        Collections.sort(opeartionLst, new Comparator<OpearterExpression>(){

			@Override
			public int compare(OpearterExpression arg0, OpearterExpression arg1) {
				if(arg0.Level>arg1.Level)return -1;
				else if(arg0.Level<arg1.Level)return 1;
				return 0;
			}});
        
        ///遍历运算符列表，按照指定运算符去运算原有表达式列表的表达式
        ///计算结果按ResultConstResult替换
        OpearterExpression opeStatement;
        for (int i = 0; i < opeartionLst.size(); i++)
        {
            opeStatement =(OpearterExpression) opeartionLst.get(i);
            int opeIndex=statementLst.indexOf(opeStatement);
            Expression arg1 = statementLst.get(opeIndex - 1);

            Object arg1Val = null;
            if (arg1 instanceof FieldExpression)
                arg1Val = ((FieldExpression)arg1  ).GetValue(datasourceCollection);
            else if (arg1 instanceof ResultConstExpression)
                arg1Val = ((ResultConstExpression)arg1).GetValue();
            else if(arg1 instanceof ConstExpression)
            	arg1Val=((ConstExpression)arg1).GetValue();
            if (opeStatement instanceof MemberOpearterExpression)
            {
                Expression arg2 = statementLst.get(opeIndex + 1);
                ResultConstExpression res = new ResultConstExpression();
                res. Result = ((MemberOpearterExpression)opeStatement  ).ComputeResult(arg1Val,arg2.StatementText) ;
                statementLst.add(opeIndex, res);
                statementLst.remove(arg1);
                statementLst.remove(arg2);
                statementLst.remove(opeStatement);
            }
            else if (opeStatement instanceof ArrayOpearterExpression)
            {
                ResultConstExpression res = new ResultConstExpression();
                res.Result = ((ArrayOpearterExpression)opeStatement  ).ComputeResult(arg1Val) ;
                statementLst.add(opeIndex, res);
                statementLst.remove(arg1);
                statementLst.remove(opeStatement);
                
            }
            else if(opeStatement instanceof FieldIndexArrayOperaterExpression)
            {
            	ResultConstExpression res = new ResultConstExpression();
                res.Result = ((FieldIndexArrayOperaterExpression)opeStatement  ).ComputeResult(arg1Val,datasourceCollection) ;
                statementLst.add(opeIndex, res);
                statementLst.remove(arg1);
                statementLst.remove(opeStatement);
            }
            else if (opeStatement instanceof StrPlusOpearterExpression)
            {
            	Expression arg2 = statementLst.get(opeIndex + 1);
                Object arg2Val = null;
                if (arg2 instanceof FieldExpression)
                    arg2Val = ((FieldExpression)arg1).GetValue(datasourceCollection);
                else if (arg2 instanceof ResultConstExpression)
                    arg2Val = ((ResultConstExpression)arg2  ).GetValue();
                else if(arg2 instanceof ConstExpression)
                	arg2Val=((ConstExpression)arg2).GetValue();
                ResultConstExpression res = new ResultConstExpression() ;
                res.Result=((StrPlusOpearterExpression)opeStatement ).ComputeResult(arg1Val,arg2Val);
                statementLst.add(opeIndex, res);
                statementLst.remove(arg1);
                statementLst.remove(arg2);
                statementLst.remove(opeStatement);
            }
        }
        ///运算完毕后表达式集合应该只剩一个表达式
        if (statementLst.size() == 1)
        {
        	Expression result = statementLst.get(0);
            if (result instanceof FieldExpression)
                return ((FieldExpression)result  ).GetValue(datasourceCollection);
            else if(result instanceof ConstExpression)
            	return ((ConstExpression)result  ).GetValue();
        }
        else
        	Log.d("", "表达式运算出错 "+statement);
        
        return null;
	}

	///解析所有表达式
    private List<Expression> GetExpression(String statement)
    {
        List<Expression> result=new ArrayList<Expression>();
        statement = statement.trim();
        Expression state;
        HashMap<String, Expression> dic = new HashMap<String, Expression>() ;
        dic.put(FieldReg, new FieldExpression() );
        dic.put(StringReg,new StringConstExpression());
        dic.put(NumberReg, new NumberConstExpression());
        dic.put(ArrayReg, new ArrayOpearterExpression());
        dic.put(MemberReg, new MemberOpearterExpression());
        dic.put(StrPlusReg, new StrPlusOpearterExpression());
        dic.put(ArrayFieldIndexReg, new FieldIndexArrayOperaterExpression());
        dic.put(DefineFieldReg, new DefineFieldExpression());
        for(Entry<String,Expression> kvp : dic.entrySet())
        {
            state = TryGetExpression(statement, kvp.getKey(),kvp.getValue());
            if (state != null)
            {
                result.add(state);
//                if(state.StatementText==null)
//                	Log.d("", "");
                String nextStatement = statement.substring(state.StatementText.length());
                result.addAll(GetExpression(nextStatement));
                break;
            }
        }
        return result;

    }

    //尝试从字符串第一个词匹配指定表达式，匹配成功返回对应实例
    private Expression TryGetExpression(String statementText,String regex,Expression instance)
    {
        String key = UUID.randomUUID().toString().toUpperCase();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(statementText);
        String tmp = matcher.replaceFirst(key);//Regex.Replace(statementText, regex, key);
//        if (tmp.StartsWith(key))
        if (tmp.startsWith(key))
        {
        	 matcher = pattern.matcher(statementText);
        	while(matcher.find())
        	{
        		instance.StatementText = matcher.group();//Regex.Matches(statementText, regex)[0].Value;
        		break;
        	}
            return instance;
        }
        return null;
    }	
	
    
	
    class Expression
    {
        public String Reg ;

        public String StatementText ;
    }

    abstract class OpearterExpression extends Expression
    {
        public int ArgCount ;

        public int Level;

        public abstract Object ComputeResult(Object... args);
    }

    class ArrayOpearterExpression extends OpearterExpression
    {

        public ArrayOpearterExpression()
        {
            Reg = "\\[\\d+\\]";
            ArgCount = 1;
            Level=2;
        }

        public  Object ComputeResult(Object... args)
        {
            String strIndex = StatementText.substring(1, StatementText.length() - 1);
            int intIndex = Integer.parseInt(strIndex);
            if (args[0].getClass().isArray())
                return  Array.get(args[0], intIndex);//(args[0] as Array).GetValue(intIndex);
//            if (args[0] is IList)
            if(TypeHelper.IsSubClassOf(args[0], ArrayList.class))
                return ((ArrayList)args[0] ).get(intIndex);
            return null;
        }
    }

    class FieldIndexArrayOperaterExpression extends OpearterExpression
    {

        public FieldIndexArrayOperaterExpression()
        {
            Reg = ArrayFieldIndexReg;
            ArgCount = 1;
            Level=2;
        }
    	
		@Override
		public Object ComputeResult(Object... args) {
			try
			{
			String fieldName = StatementText.substring(1, StatementText.length() - 1);
            int intIndex = Integer.parseInt( ((HashMap<String,Object>)args[1]).get(fieldName).toString() );
            if (args[0].getClass().isArray())
                return  Array.get(args[0], intIndex);//(args[0] as Array).GetValue(intIndex);
//            if (args[0] is IList)
            if(TypeHelper.IsSubClassOf(args[0], ArrayList.class))
                return ((ArrayList)args[0] ).get(intIndex);
			}
			catch(Exception ex)
			{
				Log.d("", "");
			}
            return null;
		}
    	
    }
    
    class MemberOpearterExpression extends OpearterExpression
    {
        public MemberOpearterExpression()
        {
            Reg = "\\.";
            ArgCount = 2;
            Level=2;
        }

        public  Object ComputeResult(Object... args)
        {
        	if(TypeHelper.IsSubClassOf(args[0], HashMap.class))
        		return ((HashMap)args[0]).get(args[1]);
            else
            {
            	try {
					Field field= args[0].getClass().getDeclaredField(args[1].toString());
					if(field==null)return null;
					field.setAccessible(true);
					Method getter=TypeHelper.Getter(args[0].getClass(), field);
					if(getter!=null)
					{
						getter.setAccessible(true);
						return getter.invoke(args[0]);
					}
					else {
						return field.get(args[0]);
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
            	return null;
            }
        }
    }

    class StrPlusOpearterExpression extends OpearterExpression
    {
        public StrPlusOpearterExpression()
        {
            Reg = "\\+";
            ArgCount = 2;
            Level=1;
        }

        public Object ComputeResult(Object... args)
        {
            return String.format("%s%s", args[0].toString(), args[1].toString());
        }
    }

    class FieldExpression extends Expression
    {
        public FieldExpression()
        {
            Reg = "((_[_a-zA-Z0-9]+)|([a-zA-Z][_a-zA-Z0-9]*))";
        }

        public Object GetValue(Object parent)
        {
        	if(TypeHelper.IsSubClassOf(parent, HashMap.class))
        		return ((HashMap)parent).get(this.StatementText);
            else
            {
            	try {
					Field field= parent.getClass().getDeclaredField(this.StatementText);
					if(field==null)return null;
					field.setAccessible(true);
					Method getter=TypeHelper.Getter(parent.getClass(), field);
					if(getter!=null)
					{
						getter.setAccessible(true);
						return getter.invoke(parent);
					}
					else {
						return field.get(parent);
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
            	return null;
            }
        }
    }

    class DefineFieldExpression extends Expression
    {
        public DefineFieldExpression()
        {
            Reg = DefineFieldReg;
        }

        public String GetValue( )
        {
            String key =StatementText; //StatementText.substring(1, StatementText.length() - 1);
            
            if(defineCollection.containsKey(key))
            	return defineCollection.get(key);
            else
            {
            	Log.d("", "缺少预处理命令参数 "+key);
            }
            return null;
        }
    }
    
    abstract class ConstExpression extends Expression
    {
        public abstract Object GetValue();
    }

    class StringConstExpression extends ConstExpression
    {
        public Object GetValue()
        {
        	String result=StatementText;
        	if(result.startsWith("\'"))
        			result=result.substring(1);
        	if(result.endsWith("\'"))
        			result=result.substring(0, result.length()-1);
            return result;
        }
    }

    class NumberConstExpression extends ConstExpression
    {
    	String IntegerReg="\\d+";
    	String FloatReg="\\d+\\.\\d+";
    	
        public Object GetValue()
        {
//            return decimal.Parse(StatementText);
        	if(StatementText.contains("."))
        		return Double.parseDouble(StatementText);
        	return Long.parseLong(StatementText);
        }
    }

    class ResultConstExpression extends ConstExpression
    {
        public Object Result;

        public Object GetValue()
        {
            return Result;
        }
    }
}
