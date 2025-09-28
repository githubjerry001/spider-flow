package org.spiderflow.core.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.core.mapper.SpiderFlowMapper;
import org.spiderflow.core.model.SpiderFlow;
import org.spiderflow.core.utils.ScheduleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * 爬虫流程执行服务
 * @author Administrator
 *
 */
@Service
public class SpiderFlowService {

	@Autowired
	private SpiderFlowMapper sfMapper;

	private static Logger logger = LoggerFactory.getLogger(SpiderFlowService.class);

	@PostConstruct
	private void initJobs(){
		//清空所有任务下次执行时间
		sfMapper.resetAllNextExecuteTime();
		//获取启用corn的任务
		List<SpiderFlow> spiderFlows = sfMapper.selectList();
		if(spiderFlows != null && !spiderFlows.isEmpty()){
			for (SpiderFlow sf : spiderFlows) {
				if("1".equals(sf.getEnabled()) && StringUtils.isNotEmpty(sf.getCron())){
					Date nextExecuteTimt = ScheduleUtils.addJob(sf);
					if (nextExecuteTimt != null) {
						sf.setNextExecuteTime(nextExecuteTimt);
						sfMapper.update(sf);
					}
				}
			}
		}
	}

	public List<SpiderFlow> selectSpiderPage(String name){
		// TODO: 实现分页查询
		return sfMapper.selectSpiderPage(name);
	}

	public int executeCountIncrement(String id, Date lastExecuteTime, Date nextExecuteTime){
		if(nextExecuteTime == null){
			return sfMapper.executeCountIncrement(id, lastExecuteTime);
		}
		return sfMapper.executeCountIncrementAndExecuteTime(id, lastExecuteTime, nextExecuteTime);

	}

	/**
	 * 重置定时任务
	 * @param id 爬虫的ID
	 * @param cron 定时器
	 */
	public void resetCornExpression(String id, String cron){
		sfMapper.resetCornExpression(id, cron, ScheduleUtils.getNextExecuteTime(cron));
		ScheduleUtils.remove(id);
		SpiderFlow spiderFlow = getById(id);
		if("1".equals(spiderFlow.getEnabled()) && StringUtils.isNotEmpty(spiderFlow.getCron())){
			ScheduleUtils.addJob(spiderFlow);
		}
	}

	public SpiderFlow getById(String id) {
		return sfMapper.selectById(id);
	}
	
	public boolean save(SpiderFlow spiderFlow){
		//解析corn,获取并设置任务的开始时间
		if(StringUtils.isNotEmpty(spiderFlow.getCron())){
			spiderFlow.setNextExecuteTime(ScheduleUtils.getNextExecuteTime(spiderFlow.getCron()));
		}
		if(StringUtils.isNotEmpty(spiderFlow.getId())){	//update 任务
			sfMapper.updateSpiderFlow(spiderFlow.getId(), spiderFlow.getName(), spiderFlow.getXml());
			ScheduleUtils.remove(spiderFlow.getId());
			spiderFlow = getById(spiderFlow.getId());
			if("1".equals(spiderFlow.getEnabled()) && StringUtils.isNotEmpty(spiderFlow.getCron())){
				ScheduleUtils.addJob(spiderFlow);
			}
		}else{//insert 任务
			String id = UUID.randomUUID().toString().replace("-", "");
			sfMapper.insertSpiderFlow(id, spiderFlow.getName(), spiderFlow.getXml());
			spiderFlow.setId(id);
		}
		return true;
	}

	public void stop(String id){
		sfMapper.resetSpiderStatus(id,"0");
		sfMapper.resetNextExecuteTimeById(id);
		ScheduleUtils.remove(id);
	}

	public void copy(String id){
		// 复制ID
		SpiderFlow spiderFlow = sfMapper.selectById(id);
		String new_id = UUID.randomUUID().toString().replace("-", "");
		sfMapper.insertSpiderFlow(new_id, spiderFlow.getName() + "-copy", spiderFlow.getXml());
	}

	public void start(String id){
		ScheduleUtils.remove(id);
		SpiderFlow spiderFlow = getById(id);
		Date nextExecuteTime = ScheduleUtils.addJob(spiderFlow);
		if (nextExecuteTime != null) {
			spiderFlow.setNextExecuteTime(nextExecuteTime);
			sfMapper.update(spiderFlow);
			sfMapper.resetSpiderStatus(id, "1");
		}
	}

	public void run(String id){
		ScheduleUtils.run(id);
	}

	public void resetExecuteCount(String id){
		sfMapper.resetExecuteCount(id);
	}
	public void remove(String id){
		sfMapper.deleteById(id);
		ScheduleUtils.remove(id);
	}

	public List<SpiderFlow> selectOtherFlows(String id){
		return sfMapper.selectOtherFlows(id);
	}

	public List<SpiderFlow> selectFlows(){
		return sfMapper.selectFlows();
	}

    /**
     * 根据表达式获取最近几次运行时间
	 * @param cron 表达式
	 * @param numTimes 几次
	 * @return
     */
	public List<String> getRecentTriggerTime(String cron,int numTimes) {
		return ScheduleUtils.getRecentTriggerTime(cron,numTimes);
	}

	public List<Long> historyList(String id){
		return ScheduleUtils.historyList(id);
	}

	public String readHistory(String id,String timestamp){
		return ScheduleUtils.readHistory(id,timestamp);
	}

	public Integer getFlowMaxTaskId(String flowId){
		return sfMapper.getFlowMaxTaskId(flowId);
	}
}
