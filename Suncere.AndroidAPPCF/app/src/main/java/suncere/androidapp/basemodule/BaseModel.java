package suncere.androidapp.basemodule;

public abstract  class BaseModel 
{
	//获取默认值
	public abstract BaseModel GetDefaultInstance();
	
	private Object Tag;

	public Object getTag() {
		return Tag;
	}

	public void setTag(Object tag) {
		Tag = tag;
	}
}
