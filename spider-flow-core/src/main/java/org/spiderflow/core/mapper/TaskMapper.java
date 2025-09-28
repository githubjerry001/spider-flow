package org.spiderflow.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.Task;

import java.util.List;

@Mapper
public interface TaskMapper {
	
	@Select("select * from sp_task")
	List<Task> selectList();
	
	@Select("select * from sp_task where id = #{id}")
	Task selectById(Integer id);
	
	@Insert("insert into sp_task(flow_id, spider_name, create_time, start_time, end_time, exception, success_count, error_count, total_count) values(#{flowId}, #{spiderName}, #{createTime}, #{startTime}, #{endTime}, #{exception}, #{successCount}, #{errorCount}, #{totalCount})")
	int insert(Task task);
	
	@Update("update sp_task set flow_id = #{flowId}, spider_name = #{spiderName}, create_time = #{createTime}, start_time = #{startTime}, end_time = #{endTime}, exception = #{exception}, success_count = #{successCount}, error_count = #{errorCount}, total_count = #{totalCount} where id = #{id}")
	int update(Task task);
	
	@Delete("delete from sp_task where id = #{id}")
	int deleteById(Integer id);
	
	// 根据流程ID分页查询任务
	@Select("select * from sp_task where flow_id = #{flowId} order by isnull(end_time) desc, end_time desc limit #{offset}, #{limit}")
	List<Task> selectPageByFlowId(String flowId, int offset, int limit);
	
	// 根据流程ID获取任务总数
	@Select("select count(*) from sp_task where flow_id = #{flowId}")
	int selectCountByFlowId(String flowId);
}