package org.spiderflow.core.model;

/**
 * 流程通知实体类
 */
public class FlowNotice {

	private final String START_FLAG = "1";

	/**
	 * 主键,对应{@link SpiderFlow}中的流程id
	 */
	private String id;
	
	/** 通知方式（email、webhook） */
	private String noticeWay;
	
	/** 通知类型（start、end、exception） */
	private String noticeType;
	
	/** 通知配置 */
	private String noticeConfig;
	
	/** 爬虫id */
	private String spiderId;
	
	/** 收件人 */
	private String recipients;
	
	/** 开始通知标志 */
	private String startNotice;
	
	/** 异常通知标志 */
	private String exceptionNotice;
	
	/** 结束通知标志 */
	private String endNotice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeConfig() {
		return noticeConfig;
	}

	public void setNoticeConfig(String noticeConfig) {
		this.noticeConfig = noticeConfig;
	}

	public String getSpiderId() {
		return spiderId;
	}

	public void setSpiderId(String spiderId) {
		this.spiderId = spiderId;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getNoticeWay() {
		return noticeWay;
	}

	public void setNoticeWay(String noticeWay) {
		this.noticeWay = noticeWay;
	}

	public String getStartNotice() {
		return startNotice;
	}

	public void setStartNotice(String startNotice) {
		this.startNotice = startNotice;
	}

	public String getExceptionNotice() {
		return exceptionNotice;
	}

	public void setExceptionNotice(String exceptionNotice) {
		this.exceptionNotice = exceptionNotice;
	}

	public String getEndNotice() {
		return endNotice;
	}

	public void setEndNotice(String endNotice) {
		this.endNotice = endNotice;
	}

	public boolean judgeStartNotice() {
		if (START_FLAG.equals(this.startNotice)) {
			return true;
		}
		return false;
	}

	public boolean judgeExceptionNotice() {
		if (START_FLAG.equals(this.exceptionNotice)) {
			return true;
		}
		return false;
	}

	public boolean judgeEndNotice() {
		if (START_FLAG.equals(this.endNotice)) {
			return true;
		}
		return false;
	}
}