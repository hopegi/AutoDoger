package suncere.androidapp.viewautobinder;

public class BinderComplierFactory {

	private static BaseBinderComplier complier=new DefaultBinderComplier();
	
	public static BaseBinderComplier GetComplier()
	{
		return complier;
	}
}
