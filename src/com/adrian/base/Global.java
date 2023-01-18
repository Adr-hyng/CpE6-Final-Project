package com.adrian.base;

import com.adrian.utils.UtilityTool;

public interface Global {
	//ResourcePath
	public static final UtilityTool util = new UtilityTool();
	public static final String assets = System.getProperty("user.dir").replace("\\", "/").concat("/res/").replace("/", "\\\\");
}
