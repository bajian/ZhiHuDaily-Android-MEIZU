package com.zhihu.daily.meizu.utils;

import android.content.Context;

import com.zhihu.daily.meizu.DailyApplication;

public class DateUtils {
	public static int dayOfWeek;

	public static String getDate(String date, Context context) {
		String today = ((DailyApplication) context.getApplicationContext())
				.getTodayString();
		if (today.equals(date)) {
			dayOfWeek = ((DailyApplication) context.getApplicationContext())
					.getWeekDay();
			if (dayOfWeek - 1 >= 1) {
				dayOfWeek -= 1;
			} else {
				dayOfWeek = 7;
			}
			return "今日热闻";
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(date.substring(4, 6) + "月");
			builder.append(date.substring(6, 8) + "日");
			switch (dayOfWeek) {
			case 1:
				builder.append("  " + "星期日");
				break;
			case 2:
				builder.append("  " + "星期一");
				break;
			case 3:
				builder.append("  " + "星期二");
				break;
			case 4:
				builder.append("  " + "星期三");
				break;
			case 5:
				builder.append("  " + "星期四");
				break;
			case 6:
				builder.append("  " + "星期五");
				break;
			case 7:
				builder.append("  " + "星期六");
				break;
			}
			

			if (dayOfWeek - 1 >= 1) {
				dayOfWeek -= 1;
			} else {
				dayOfWeek = 7;
			}

			return builder.toString();
		}
	}
}
