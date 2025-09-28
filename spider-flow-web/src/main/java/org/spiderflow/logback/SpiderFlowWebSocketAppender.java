package org.spiderflow.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpiderFlowWebSocketAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	@Override
	protected void append(ILoggingEvent event) {
		// 尝试获取SpiderContext，如果类不存在则跳过
		try {
			Class<?> contextHolderClass = Class.forName("org.spiderflow.context.SpiderContextHolder");
			Object context = contextHolderClass.getMethod("get").invoke(null);
			if (context != null) {
				Class<?> socketContextClass = Class.forName("org.spiderflow.model.SpiderWebSocketContext");
				if (socketContextClass.isInstance(context)) {
					Object[] argumentArray = event.getArgumentArray();
					List<Object> arguments = argumentArray == null ? Collections.emptyList() : new ArrayList<>(Arrays.asList(argumentArray));
					Object throwableProxy = event.getThrowableProxy();
					if (throwableProxy != null) {
						arguments.add(throwableProxy);
					}
					Object log = Class.forName("org.spiderflow.model.SpiderLog")
							.getConstructor(String.class, String.class, List.class)
							.newInstance(event.getLevel().levelStr.toLowerCase(), event.getMessage(), arguments);
					socketContextClass.getMethod("log", log.getClass()).invoke(context, log);
				}
			}
		} catch (Exception e) {
			// 类不存在或方法调用失败，跳过日志记录
		}
	}
}
