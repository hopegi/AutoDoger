package suncere.androidapp.tableupgradeter;

import java.util.List;

public interface IAutoUpgradeTable  extends IBaseUpgradeInterface{
	
	List<String> GetUpgradeSQL();
	
	IAutoUpgradeTable GetParentUpdrade();
}
