package com.zhihu.daily.meizu.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.zhihu.daily.meizu.model.Theme;

import android.content.Context;

public class JsoupUtils {
	public static String getPage(String HTMLString,Context context){
		Document doc = Jsoup.parse(HTMLString);
		Element head = doc.head();
		head.append("<meta charset='utf-8'>");
		head.append("<meta name='viewport' content='width=device-width,user-scalable=no'/>");
		head.append("<link href='file:///android_asset/news_qa.6.css' rel='stylesheet'/>");
		if(PerferenceHelper.getTheme(context)==Theme.DARK){
			if(PerferenceHelper.getIsLarge(context)){
				doc.select("body").attr("class", "night large");
			}else{
				doc.select("body").attr("class", "night");
			}
		}else{
			if(PerferenceHelper.getIsLarge(context)){
				doc.select("body").attr("class", "large");
			}
		}
		Element div = doc.select(".img-place-holder").first();
		if (div != null)
			div.attr("class", "");
	
		return doc.toString();
	}
}
