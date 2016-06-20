package com.hawk.library.common.setting;

import android.support.v4.util.ArrayMap;

import java.io.Serializable;
import java.util.Map;

public class Setting extends SettingBean implements Serializable {

	private static final long serialVersionUID = 4801654811733634325L;
	
	private ArrayMap<String, SettingExtra> extras;

	public Setting() {
		extras = new ArrayMap<String, SettingExtra>();
	}

	public Map<String, SettingExtra> getExtras() {
		return extras;
	}

	public void setExtras(ArrayMap<String, SettingExtra> extras) {
		this.extras = extras;
	}
	
}
