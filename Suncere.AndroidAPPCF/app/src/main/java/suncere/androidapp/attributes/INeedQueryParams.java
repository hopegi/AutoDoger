package suncere.androidapp.attributes;

import java.util.HashMap;

import suncere.androidapp.autobasemodule.AutoBaseModel;

public interface INeedQueryParams {

	void QueryParam(AutoBaseModel model, HashMap<String, Object> otherPara);
}
