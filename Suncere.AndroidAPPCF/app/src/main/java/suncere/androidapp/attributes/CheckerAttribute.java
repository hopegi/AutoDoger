package suncere.androidapp.attributes;

import suncere.androidapp.autobasemodule.IChecker;

public class CheckerAttribute implements IClassAttribute{

	private IChecker checker;
	
	public CheckerAttribute(IChecker checker)
	{
		this.checker=checker;
	}
	
	public IChecker Checker()
	{
		return this.checker;
	}
}
