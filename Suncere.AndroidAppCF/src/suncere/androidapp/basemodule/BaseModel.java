package suncere.androidapp.basemodule;

public abstract  class BaseModel 
{
	//��ȡĬ��ֵ
	public abstract BaseModel GetDefaultInstance();
	
	private Object Tag;

	public Object getTag() {
		return Tag;
	}

	public void setTag(Object tag) {
		Tag = tag;
	}
}
