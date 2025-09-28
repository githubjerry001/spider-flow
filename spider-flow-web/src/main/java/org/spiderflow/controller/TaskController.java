package org.spiderflow.controller;

import org.spiderflow.context.SpiderContext;
import org.spiderflow.core.job.SpiderJob;
import org.spiderflow.core.model.Task;
import org.spiderflow.core.model.Page;
import org.spiderflow.core.service.TaskService;
import org.spiderflow.model.JsonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@RequestMapping("/list")
	public Page<Task> list(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "limit", defaultValue = "1") Integer size,String flowId){
		return taskService.page(page, size, flowId);
	}

	/**
	 * 停止执行任务
	 * @param id
	 * @return
	 */
	@RequestMapping("/stop")
	public JsonBean<Boolean> stop(Integer id){
		SpiderContext context = SpiderJob.getSpiderContext(id);
		if(context != null){
			context.setRunning(false);
		}
		return new JsonBean<>(context != null);
	}

	@RequestMapping("/remove")
	public JsonBean<Boolean> remove(Integer id){
		//删除任务记录之前先停止
		SpiderContext context = SpiderJob.getSpiderContext(id);
		if(context != null){
			context.setRunning(false);
		}
		return new JsonBean<>(taskService.removeById(id));
	}
}