package org.spiderflow.core.utils;

import org.spiderflow.core.job.SpiderJobManager;
import org.spiderflow.core.model.SpiderFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleUtils {
	
	@Autowired
	private static SpiderJobManager spiderJobManager;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static Date addJob(SpiderFlow spiderFlow) {
		return spiderJobManager.addJob(spiderFlow);
	}
	
	public static void remove(String id) {
		spiderJobManager.remove(id);
	}
	
	public static void run(String id) {
		spiderJobManager.run(id);
	}
	
	public static Date getNextExecuteTime(String cron) {
		// 这里应该解析cron表达式并计算下次执行时间
		// 简化实现，返回当前时间
		return new Date();
	}
	
	public static List<String> getRecentTriggerTime(String cron, int numTimes) {
		List<String> result = new ArrayList<>();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		for (int i = 0; i < numTimes; i++) {
			result.add(sdf.format(new Date(now.getTime() + i * 60000))); // 每隔1分钟
		}
		return result;
	}
	
	public static List<Long> historyList(String id) {
		// 简化实现，返回空列表
		return new ArrayList<>();
	}
	
	public static String readHistory(String id, String timestamp) {
		// 简化实现，返回空字符串
		return "";
	}
}