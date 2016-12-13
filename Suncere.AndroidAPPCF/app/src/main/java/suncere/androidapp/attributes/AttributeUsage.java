package suncere.androidapp.attributes;

public enum AttributeUsage {

	None(0),
	ResultDataHandle(1),
	NetDataHandle(2);
	
	private int value;
	
	AttributeUsage(int value)
	{
		this.value=value;
	}
	
	public int Value()
	{
		return this.value;
	}
}
