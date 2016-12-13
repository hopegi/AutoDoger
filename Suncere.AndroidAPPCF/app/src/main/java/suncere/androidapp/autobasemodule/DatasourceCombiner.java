package suncere.androidapp.autobasemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class DatasourceCombiner {


	List<HashMap<String,Object>> combinDatasourceSetting;
	
	public DatasourceCombiner()
	{
		combinDatasourceSetting=new ArrayList<HashMap<String,Object>>();
	}
	
	public void AddCombineDatasource(String newDatasourceName,String ds1,String ds2,String... keys)
	{
		HashMap<String,Object> combineSetting=new HashMap<String,Object>();
		combineSetting.put("NewName", newDatasourceName);
		combineSetting.put("DatasourceName1", ds1);
		combineSetting.put("DatasourceName2", ds2);
		combineSetting.put("Keys", keys);
		this.combinDatasourceSetting.add( combineSetting);
	}
	
	public void CombineData(HashMap<String,List<HashMap<String,Object>>> datasourceCollection)
	{
		List<HashMap<String,Object>> ds1;
		List<HashMap<String,Object>>ds2;
		List<HashMap<String,Object>> newDs;
		String[] keys;
		for(HashMap<String,Object> combineSetting: this.combinDatasourceSetting)
		{
			newDs=datasourceCollection.get(combineSetting.get("NewName"));
			ds1=datasourceCollection.get(combineSetting.get("DatasourceName1"));
			ds2=datasourceCollection.get(combineSetting.get("DatasourceName2"));
			keys=(String[]) combineSetting.get("Keys");
			
			newDs.clear();
			HashMap<String,Object> newItem;
			for(HashMap<String,Object> item: ds1)
			{
				newItem=new HashMap<String,Object>();
				newItem.putAll(item);
				newDs.add(newItem);
				for(HashMap<String,Object> item2:ds2)
				{
					boolean isMatch=true;
					for(int i=0;i<keys.length;i++)
					{
						if( ! item.get(keys[i]).equals(item2.get(keys[i]))  )
						{
							isMatch=false;
							break;
						}
					}
					if(isMatch)
					{
						String newKey;
						for(Entry<String,Object> kvp :item2.entrySet()    )
						{
							newKey=kvp.getKey()+"_"+combineSetting.get("DatasourceName2");
							if(!newItem.containsKey(newKey))
								newItem.put(newKey, kvp.getValue());
						}
						break;
					}
				}
			}
		}
	}
}
