package org.spiderflow.core.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.SpiderFlow;

@Mapper
public interface SpiderFlowMapper {
	
	@Select("select * from sp_flow")
	List<SpiderFlow> selectList();
	
	@Select("select * from sp_flow where id = #{id}")
	SpiderFlow selectById(String id);
	
	@Insert("insert into sp_flow(id,name,xml,enabled,create_date) values(#{id},#{name},#{xml},#{enabled},#{createDate})")
	int insert(SpiderFlow spiderFlow);
	
	@Update("update sp_flow set name = #{name},xml = #{xml},enabled = #{enabled},create_date = #{createDate} where id = #{id}")
	int update(SpiderFlow spiderFlow);
	
	@Delete("delete from sp_flow where id = #{id}")
	int deleteById(String id);

	@Select({
			"<script>",
				"select",
					"id,name,enabled,last_execute_time,next_execute_time,cron,create_date,execute_count,",
					"(select count(*) from sp_task where flow_id = sf.id and end_time is null) running",
				"from sp_flow sf",
				"<if test=\"name != null and name != ''\">",
					"where name like concat('%',#{name},'%')",
				"</if>",
				"order by create_date desc",
			"</script>"
	})
	List<SpiderFlow> selectSpiderPage(@Param("name") String name);

	@Insert("insert into sp_flow(id,name,xml,enabled) values(#{id},#{name},#{xml},'0')")
	int insertSpiderFlow(@Param("id") String id, @Param("name") String name, @Param("xml") String xml);
	
	@Update("update sp_flow set name = #{name},xml = #{xml} where id = #{id}")
	int updateSpiderFlow(@Param("id") String id, @Param("name") String name, @Param("xml") String xml);
	
	@Update("update sp_flow set execute_count = 0 where id = #{id}")
	int resetExecuteCount(@Param("id") String id);
	
	@Update("update sp_flow set execute_count = ifnull(execute_count,0) + 1,last_execute_time = #{lastExecuteTime},next_execute_time = #{nextExecuteTime} where id = #{id}")
	int executeCountIncrementAndExecuteTime(@Param("id") String id, @Param("lastExecuteTime") Date lastExecuteTime, @Param("nextExecuteTime") Date nextExecuteTime);
	
	@Update("update sp_flow set execute_count = ifnull(execute_count,0) + 1,last_execute_time = #{lastExecuteTime} where id = #{id}")
	int executeCountIncrement(@Param("id") String id, @Param("lastExecuteTime") Date lastExecuteTime);
	
	@Update("update sp_flow set cron = #{cron},next_execute_time = #{nextExecuteTime} where id = #{id}")
	int resetCornExpression(@Param("id") String id, @Param("cron") String cron, @Param("nextExecuteTime") Date nextExecuteTime);
	
	@Update("update sp_flow set enabled = #{enabled} where id = #{id}")
	int resetSpiderStatus(@Param("id") String id, @Param("enabled") String enabled);

	@Update("update sp_flow set next_execute_time = null where id = #{id}")
	int resetNextExecuteTimeById(@Param("id") String id);

	@Update("update sp_flow set next_execute_time = null")
	int resetAllNextExecuteTime();
	
	@Select("select id,name from sp_flow")
	List<SpiderFlow> selectFlows();
	
	@Select("select id,name from sp_flow where id != #{id}")
	List<SpiderFlow> selectOtherFlows(@Param("id") String id);

	@Select("select max(a.id) from `sp_task` a left join sp_flow b on a.flow_id = b.id where b.id = #{id}")
	Integer getFlowMaxTaskId(@Param("id")String id);
	
	@Select("select COUNT(id) from sp_flow where id = #{id}")
	Integer getCountById(@Param("id")String id);
}
