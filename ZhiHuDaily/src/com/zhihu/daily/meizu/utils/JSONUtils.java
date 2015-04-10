package com.zhihu.daily.meizu.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class JSONUtils {
	public static <T> List<T> getListObjectFromJSON(String jsonStr,Class<T> cls){
		  return JSON.parseArray(jsonStr, cls);
	}
	
	public static <T> T getObjectFromJSON(String jsonStr,Class<T> cls){
		  return  JSON.parseObject(jsonStr, cls);
	}
}
