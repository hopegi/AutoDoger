package suncere.androidapp.autobasemodule;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidapp.attributes.CheckerAttribute;
import suncere.androidapp.attributes.IClassAttribute;
import suncere.androidapp.basemodule.BaseBLL;
import suncere.androidapp.basemodule.BaseDAL;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.TypeHelper;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import dalvik.system.DexFile;

public class PluginLoader {

	public final static String PACKAGE_NAME_PREFIX="suncere.androidapp.";
	public final static String CORE_PLUGIN_NAME_V2="suncere.androidapp.autobasemodule";
	public final static String CORE_PLUGIN_NAME_V1="suncere.androidapp.basemodule";
	public final static List<String> EXCEPT_PACKAGE_NAMES=new ArrayList<String>();
	
	private final static HashMap<String, List<Class<?>>> pluginMap=new  HashMap<String, List<Class<?>>>();
	
	public AutoBaseBLL InitPlugin(AutoBaseModel model)
	{
		String packageName=model.getClass().getPackage().getName();
		if(!pluginMap.containsKey(packageName))return null;
		
		List<Class<?>> classLst=pluginMap.get(packageName);
		Class<?> tmpClass=null;
		
		//event
		IPluginLoadingEvent event=null;
		tmpClass= GetFirstClassByParent(classLst, IPluginLoadingEvent.class);
		if(tmpClass!=null)
			event=(IPluginLoadingEvent) TypeHelper.NewIntance(tmpClass);
		
		//SqlStatementProvider
		if(event!=null)
		{
			SqlStatementServiceProvider.Current().AddServiceSeries(packageName, event.OnLoadStatementProvider());
		}
		
		//Regist DataLoaderConfig
		for(Class<?> classType : classLst)
		{
			 if(TypeHelper.IsSubClassOf(classType, DataLoaderConfigBase.class))
			 {
					LoadingConfigProvider.Current().RegistConfig((DataLoaderConfigBase)TypeHelper.NewIntance(classType));
			 }
		}
		
		//INetDataHandler
		INetDataHandler netDataHandler=null;
		if(event!=null)
			netDataHandler=event.GetNetDataHandler();
		if(netDataHandler==null)
		{
			tmpClass=GetFirstClassByParent(classLst,INetDataHandler.class);
			if(tmpClass!=null)
				netDataHandler=(INetDataHandler)TypeHelper.NewIntance(tmpClass);
		}
		
		//IResultDataHandler
		IResultDataHandler resultDataHandler=null;
		if(event!=null)
			resultDataHandler=event.GetResultDataHandler();
		if(resultDataHandler==null)
		{
			tmpClass=this.GetFirstClassByParent(classLst, IResultDataHandler.class);
			if(tmpClass!=null)
				resultDataHandler=(IResultDataHandler) TypeHelper.NewIntance(tmpClass);
		}
		
		//checker
		IChecker checker=null;
		for(IClassAttribute attribute : model.ClassAttributes())
		{
			if(  TypeHelper.IsSubClassOf(attribute, CheckerAttribute.class) )
			{
				checker= ((CheckerAttribute)attribute).Checker();
				break;
			}
		}
		if(checker==null && event!=null)
			checker=event.GetChecker();
		if(checker==null)
		{
			tmpClass=GetFirstClassByParent(classLst, IChecker.class);
			if(tmpClass!=null)
				checker=(IChecker) TypeHelper.NewIntance(tmpClass);
		}
		if(checker==null)
			checker=new LastTimePointChecker();
		
		//maindal
		AutoBaseDAL mainDAL=null;
		if(event!=null)
			mainDAL=event.GetMainDAL();
		if(mainDAL==null)
		{
			tmpClass=GetFirstClassByParent(classLst, AutoBaseDAL.class);
			if(tmpClass!=null)
				mainDAL=(AutoBaseDAL) TypeHelper.NewIntance(tmpClass, new Class[]{ Context.class }, new Object[]{ SuncereApplication.CurrentApplication() });
		}
		if(mainDAL==null)
			mainDAL=new AutoBaseDAL(SuncereApplication.CurrentApplication() ) ;
		
		//loader
		AutoBaseDataLoader loader=null;
		if(event!=null)
			loader=event.GetLoader();
		if(loader==null)
		{
			tmpClass=GetFirstClassByParent(classLst, AutoBaseDataLoader.class);
			if(tmpClass!=null)
				loader=(AutoBaseDataLoader) TypeHelper.NewIntance(tmpClass);
		}
		if(loader==null)
			loader=new AutoBaseDataLoader();
		

		PluginContext context=new PluginContext();
		context.setModel(model);
		context.setChecker(checker);
		context.setLoader(loader);
		context.setMainDal(mainDAL);
		context.setNetDataHandler(netDataHandler);
		context.setResultDataHandler(resultDataHandler);
		//bll
		AutoBaseBLL result=null;
		tmpClass =GetFirstClassByParent(classLst, AutoBaseBLL.class);
		if(tmpClass!=null)
		{
			result=(AutoBaseBLL) TypeHelper.NewIntance(tmpClass,new Class[]{ PluginContext.class }, new Object[]{ context });
			Field[] fields= tmpClass.getDeclaredFields();
			for(Field field : fields)
			{
				//获取 BaseDAL 和 AutoBaseDAL 的字段 实例化
				if(TypeHelper.IsSubClassOf(field.getDeclaringClass(), BaseDAL.class))
				{
					Object dalInstance=TypeHelper.NewIntance(field.getDeclaringClass(), new Class[]{ Context.class }, new Object[]{ SuncereApplication.CurrentApplication() });
					Method setter= TypeHelper.Setter(field.getDeclaringClass(), field);
					if(setter!=null)
					{
						try {
							setter.invoke(result, dalInstance);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					else {
						try {
							field.set(result, dalInstance);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}
		else {
			result=(AutoBaseBLL) TypeHelper.NewIntance(AutoBaseBLL.class, new Class[]{ PluginContext.class }, new Object[]{ context});
		}
		
		//bll inner dal
		return result;
	}
	
	private Class<?> GetFirstClassByParent(List<Class<?>> classLst,Class<?> parentClass)
	{
		Class<?> result=null;
		for(Class<?> cls : classLst)
		{
			if(TypeHelper.IsSubClassOf(cls,parentClass))
			{
				result=cls;
				break;
			}
		}
		return result;
	}
	
	///要初始化各个Plugin
	///对Event内容要补充
	
	private static PluginLoader _default=new PluginLoader();
	
	public static PluginLoader Current()
	{
		return _default;
	}
	
	private PluginLoader()
	{
		
	}
	
	private boolean HasScan=false;
	
	public void ScanAllPlugin()
	{
		if(HasScan)return;
		HasScan=true;
		EXCEPT_PACKAGE_NAMES.add(CORE_PLUGIN_NAME_V2);
		EXCEPT_PACKAGE_NAMES.add(CORE_PLUGIN_NAME_V1);
		

		String path;
		try {
			path = SuncereApplication.CurrentApplication() .getPackageManager().getApplicationInfo( SuncereApplication.CurrentApplication() .getPackageName(), 0).sourceDir;
			DexFile dexfile = new DexFile(path); 
			Enumeration entries = dexfile.entries();
			List<String> classNameLst=new ArrayList<String>();
			String className;
			while (entries.hasMoreElements())
			{ 
				className=(String) entries.nextElement();
				if(className.startsWith(PACKAGE_NAME_PREFIX)&&!EXCEPT_PACKAGE_NAMES.contains(className))
				{
					classNameLst.add( className );
				}
			}
			for(int i=0;i<classNameLst.size();i++)
			{
				className=classNameLst.get(i);
				try {
					Class<?> cls= Class.forName(className);
					String packageName=cls.getPackage().getName();
					if(!pluginMap.containsKey(packageName))
						pluginMap.put(packageName, new ArrayList<Class<?>>());
					pluginMap.get(packageName).add(cls);
				} catch (ClassNotFoundException e) {
					Log.e("", "ClassNotFoundException "+className);
					e.printStackTrace();
				}
			}
			for(Entry<String,List<Class<?>>> kvp : pluginMap.entrySet())
		        Log.d("", "PackageName "+kvp.getKey());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
        
    }
}
