package suncere.androidapp.autobasemodule;

public class CheckerContext {

	private AutoBaseDAL dal;
	
	private AutoBaseModel model;

	public AutoBaseDAL getDal() {
		return dal;
	}

	public void setDal(AutoBaseDAL dal) {
		this.dal = dal;
	}

	public AutoBaseModel getModel() {
		return model;
	}

	public void setModel(AutoBaseModel model) {
		this.model = model;
	}
}
