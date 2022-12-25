package com.adrian;

import com.adrian.utils.UtilityTool;

public interface GlobalTool {
	//ResourcePath
	public static final UtilityTool utilityTool = new UtilityTool();
	public static final String assetsDirectory = System.getProperty("user.dir").replace("\\", "/").concat("/res/").replace("/", "\\\\");
}
