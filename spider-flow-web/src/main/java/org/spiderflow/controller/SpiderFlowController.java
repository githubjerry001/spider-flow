package org.spiderflow.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.core.Spider;
import org.spiderflow.core.job.SpiderJobManager;
import org.spiderflow.core.model.SpiderFlow;
import org.spiderflow.core.service.SpiderFlowService;
import org.spiderflow.core.utils.FileUtils;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.model.SpiderLog;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.model.SpiderOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 爬虫流程Controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/spider")
public class SpiderFlowController {
	
	private static Logger logger = LoggerFactory.getLogger(SpiderFlowController.class);
	
	@Autowired
	private SpiderFlowService spiderFlowService;
	
	@Autowired
	private Spider spider;
	
	@Autowired
	private SpiderJobManager spiderJobManager;
	
	@Value("${spider.workspace}")
	private String workspace;
	
	@PostConstruct
	private void init(){
		// FileUtils.setBasePath(workspace);
	}
	
	/**
	 * 分页查询爬虫
	 * @param page 页数
	 * @param size 每页显示条数
	 * @param name 爬虫名称
	 * @return Page<SpiderFlow>
	 */
	@PostMapping("/list")
	public List<SpiderFlow> list(@RequestParam(name = "name", defaultValue = "") String name) {
		return spiderFlowService.selectSpiderPage(name);
	}
	
	/**
	 * 根据id获取爬虫
	 * @param id 爬虫id
	 * @return SpiderFlow
	 */
	@GetMapping("/get")
	public SpiderFlow get(@RequestParam(name = "id")String id){
		return spiderFlowService.getById(id);
	}
	
	/**
	 * 保存爬虫
	 * @param id 爬虫id
	 * @param name 爬虫名称
	 * @param xml 爬虫XML配置
	 * @return String 返回爬虫ID
	 */
	@PostMapping("/save")
	public String save(@RequestParam(name = "id", required = false) String id, 
					   @RequestParam(name = "name") String name, 
					   @RequestParam(name = "xml") String xml){
		try {
			SpiderFlow spiderFlow = new SpiderFlow();
			spiderFlow.setId(id);
			spiderFlow.setName(name);
			spiderFlow.setXml(xml);
			
			boolean success = spiderFlowService.save(spiderFlow);
			if (success) {
				// 如果是新增，返回生成的ID；如果是更新，返回原ID
				return spiderFlow.getId();
			} else {
				throw new RuntimeException("保存失败");
			}
		} catch (Exception e) {
			logger.error("保存爬虫失败", e);
			throw new RuntimeException("保存爬虫失败: " + e.getMessage());
		}
	}
	
	/**
	 * 删除爬虫
	 * @param id 爬虫id
	 * @return boolean
	 */
	@PostMapping("/remove")
	public boolean remove(@RequestParam(name = "id")String id){
		spiderFlowService.remove(id);
		return true;
	}
	
	/**
	 * 复制爬虫
	 * @param id 爬虫id
	 * @return boolean
	 */
	@PostMapping("/copy")
	public boolean copy(@RequestParam(name = "id")String id){
		spiderFlowService.copy(id);
		return true;
	}
	
	/**
	 * 运行爬虫
	 * @param id 爬虫id
	 * @return boolean
	 */
	@PostMapping("/run")
	public boolean run(@RequestParam(name = "id")String id){
		spiderFlowService.run(id);
		return true;
	}
	
	/**
	 * 启动爬虫
	 * @param id 爬虫id
	 * @return boolean
	 */
	@PostMapping("/start")
	public boolean start(@RequestParam(name = "id")String id){
		spiderFlowService.start(id);
		return true;
	}
	
	/**
	 * 停止爬虫
	 * @param id 爬虫id
	 * @return boolean
	 */
	@PostMapping("/stop")
	public boolean stop(@RequestParam(name = "id")String id){
		spiderFlowService.stop(id);
		return true;
	}
	
	/**
	 * 重置定时任务
	 * @param id 爬虫id
	 * @param cron 定时器
	 * @return boolean
	 */
	@PostMapping("/resetCorn")
	public boolean resetCorn(@RequestParam(name = "id")String id,@RequestParam(name = "cron")String cron){
		spiderFlowService.resetCornExpression(id, cron);
		return true;
	}
	
	/**
	 * 重置执行次数
	 * @param id 爬虫id
	 * @return boolean
	 */
	@PostMapping("/resetExecuteCount")
	public boolean resetExecuteCount(@RequestParam(name = "id")String id){
		spiderFlowService.resetExecuteCount(id);
		return true;
	}
	
	/**
	 * 获取表达式最近几次运行时间
	 * @param cron 表达式
	 * @param numTimes 几次
	 * @return List<String>
	 */
	@PostMapping("/getRecentTriggerTime")
	public List<String> getRecentTriggerTime(@RequestParam(name = "cron")String cron,@RequestParam(name = "numTimes",defaultValue = "5")int numTimes){
		return spiderFlowService.getRecentTriggerTime(cron, numTimes);
	}
	
	/**
	 * 获取所有爬虫
	 * @return List<SpiderFlow>
	 */
	@GetMapping("/flows")
	public List<SpiderFlow> flows(){
		return spiderFlowService.selectFlows();
	}
	
	/**
	 * 获取插件配置
	 * @return List<PluginConfig>
	 */
	@GetMapping("/pluginConfigs")
	public List<Object> pluginConfigs(){
		// 返回空列表，表示没有额外的插件配置
		// 在实际项目中，这里可以返回动态插件的配置信息
		return new ArrayList<>();
	}
	
	/**
	 * 获取其他爬虫
	 * @param id 爬虫id
	 * @return List<SpiderFlow>
	 */
	@GetMapping("/otherFlows")
	public List<SpiderFlow> otherFlows(@RequestParam(name = "id")String id){
		return spiderFlowService.selectOtherFlows(id);
	}
	
	/**
	 * 获取历史记录
	 * @param id 爬虫id
	 * @return List<Long>
	 */
	@GetMapping("/historyList")
	public List<Long> historyList(@RequestParam(name = "id")String id){
		return spiderFlowService.historyList(id);
	}
	
	/**
	 * 读取历史记录
	 * @param id 爬虫id
	 * @param timestamp 时间戳
	 * @return String
	 */
	@GetMapping("/readHistory")
	public String readHistory(@RequestParam(name = "id")String id,@RequestParam(name = "timestamp")String timestamp){
		return spiderFlowService.readHistory(id, timestamp);
	}
	
	/**
	 * 获取爬虫执行日志
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderLog>
	 */
	@GetMapping("/logs")
	public List<SpiderLog> logs(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行日志的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行结果
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderOutput>
	 */
	@GetMapping("/outputs")
	public List<SpiderOutput> outputs(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行结果的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行异常
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderLog>
	 */
	@GetMapping("/errors")
	public List<SpiderLog> errors(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行异常的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodes")
	public List<SpiderNode> nodes(@RequestParam(name = "id")String id){
		// TODO: 实现获取爬虫执行节点的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTree")
	public List<SpiderNode> nodeTree(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithLog")
	public List<SpiderNode> nodeTreeWithLog(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树和日志的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithOutput")
	public List<SpiderNode> nodeTreeWithOutput(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树和输出的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithError")
	public List<SpiderNode> nodeTreeWithError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAll")
	public List<SpiderNode> nodeTreeWithAll(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树和所有信息的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithLogAndOutput")
	public List<SpiderNode> nodeTreeWithLogAndOutput(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、日志和输出的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithLogAndError")
	public List<SpiderNode> nodeTreeWithLogAndError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、日志和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithOutputAndError")
	public List<SpiderNode> nodeTreeWithOutputAndError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、输出和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndLog")
	public List<SpiderNode> nodeTreeWithAllAndLog(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息和日志的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndOutput")
	public List<SpiderNode> nodeTreeWithAllAndOutput(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息和输出的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndError")
	public List<SpiderNode> nodeTreeWithAllAndError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndLogAndOutput")
	public List<SpiderNode> nodeTreeWithAllAndLogAndOutput(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息、日志和输出的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndLogAndError")
	public List<SpiderNode> nodeTreeWithAllAndLogAndError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息、日志和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndOutputAndError")
	public List<SpiderNode> nodeTreeWithAllAndOutputAndError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息、输出和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndLogAndOutputAndError")
	public List<SpiderNode> nodeTreeWithAllAndLogAndOutputAndError(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息、日志、输出和错误的方法
		return new ArrayList<>();
	}
	
	/**
	 * 获取爬虫执行节点
	 * @param id 爬虫id
	 * @param taskId 任务id
	 * @return List<SpiderNode>
	 */
	@GetMapping("/nodeTreeWithAllAndLogAndOutputAndErrorAndNode")
	public List<SpiderNode> nodeTreeWithAllAndLogAndOutputAndErrorAndNode(@RequestParam(name = "id")String id,@RequestParam(name = "taskId",defaultValue = "-1")Integer taskId){
		// TODO: 实现获取爬虫执行节点树、所有信息、日志、输出、错误和节点的方法
		return new ArrayList<>();
	}
}