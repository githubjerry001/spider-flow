package org.spiderflow.core.service;

import java.util.List;

import org.spiderflow.core.mapper.TaskMapper;
import org.spiderflow.core.model.Task;
import org.spiderflow.core.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
	
	@Autowired
	private TaskMapper taskMapper;
	
	public boolean saveOrUpdate(Task task) {
		if (task.getId() != null) {
			return taskMapper.update(task) > 0;
		} else {
			return taskMapper.insert(task) > 0;
		}
	}
	
	public boolean save(Task task) {
		return taskMapper.insert(task) > 0;
	}
	
	public boolean removeById(Integer id) {
		return taskMapper.deleteById(id) > 0;
	}
	
	// 分页查询方法
	public Page<Task> page(int current, int size, String flowId) {
		int offset = (current - 1) * size;
		List<Task> records = taskMapper.selectPageByFlowId(flowId, offset, size);
		int total = taskMapper.selectCountByFlowId(flowId);
		return new Page<>(records, total, size, current);
	}
}