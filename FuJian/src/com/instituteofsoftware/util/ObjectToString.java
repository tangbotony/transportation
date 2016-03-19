package com.instituteofsoftware.util;

import com.alibaba.fastjson.JSON;

public class ObjectToString {
	public static String createJsonString(Object value)
	{
		return  JSON.toJSONString(value);
	}
}

