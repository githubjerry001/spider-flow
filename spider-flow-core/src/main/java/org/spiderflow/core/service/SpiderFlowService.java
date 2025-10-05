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
		try {
			logger.info("SpiderFlowService.save - ID: {}, Name: {}", spiderFlow.getId(), spiderFlow.getName());
			
			// 设置创建时间
			if (spiderFlow.getCreateDate() == null) {
				spiderFlow.setCreateDate(new Date());
			}
			
			//解析corn,获取并设置任务的开始时间
			if(StringUtils.isNotEmpty(spiderFlow.getCron())){
				spiderFlow.setNextExecuteTime(ScheduleUtils.getNextExecuteTime(spiderFlow.getCron()));
			}
			
			if(StringUtils.isNotEmpty(spiderFlow.getId())){	//update 任务
				logger.info("更新爬虫 - ID: {}", spiderFlow.getId());
				int result = sfMapper.updateSpiderFlow(spiderFlow.getId(), spiderFlow.getName(), spiderFlow.getXml());
				logger.info("更新结果: {}", result);
				
				ScheduleUtils.remove(spiderFlow.getId());
				SpiderFlow updated = getById(spiderFlow.getId());
				if(updated != null && "1".equals(updated.getEnabled()) && StringUtils.isNotEmpty(updated.getCron())){
					ScheduleUtils.addJob(updated);
				}
				return result > 0;
			}else{//insert 任务
				String id = UUID.randomUUID().toString().replace("-", "");
				logger.info("新增爬虫 - 生成ID: {}", id);
				int result = sfMapper.insertSpiderFlow(id, spiderFlow.getName(), spiderFlow.getXml());
				logger.info("新增结果: {}", result);
				
				spiderFlow.setId(id);
				return result > 0;
			}
		} catch (Exception e) {
			logger.error("保存爬虫异常", e);
			return false;
		}
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
		try {
			logger.info("SpiderFlowService.remove - ID: {}", id);
			int result = sfMapper.deleteById(id);
			logger.info("删除结果: {}", result);
			ScheduleUtils.remove(id);
			if (result == 0) {
				logger.warn("删除爬虫失败，可能不存在 - ID: {}", id);
			}
		} catch (Exception e) {
			logger.error("删除爬虫异常 - ID: {}", id, e);
			throw new RuntimeException("删除失败: " + e.getMessage());
		}
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