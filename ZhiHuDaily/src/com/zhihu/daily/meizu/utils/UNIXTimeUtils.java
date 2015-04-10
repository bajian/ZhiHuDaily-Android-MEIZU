package com.zhihu.daily.meizu.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;

//评论时间为UNIX时间
@SuppressLint("SimpleDateFormat")
public class UNIXTimeUtils {
	public static String converttodate(long t) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		return time.format(t * 1000);
	}

	public static CharSequence converttotime(long t) {
		SimpleDateFormat time = new SimpleDateFormat("HH时mm分ss秒");
		return time.format(t * 1000);
	}

	public static String toLocalTime(String unix) {
		Long timestamp = Long.parseLong(unix) * 1000;
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new java.util.Date(timestamp));
		return date;
	}

	public static String toUnixTime(String local) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String unix = "";
		try {
			unix = df.parse(local).getTime() + "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return unix;
	}
}
