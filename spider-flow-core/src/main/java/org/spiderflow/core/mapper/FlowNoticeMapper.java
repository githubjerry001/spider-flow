package org.spiderflow.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.FlowNotice;

@Mapper
public interface FlowNoticeMapper {
	
	@Select("select * from sp_flow_notice where id = #{id}")
	FlowNotice selectById(String id);
	
	@Insert("insert into sp_flow_notice(id, recipients, notice_way, start_notice, end_notice, exception_notice) values(#{id}, #{recipients}, #{noticeWay}, #{startNotice}, #{endNotice}, #{exceptionNotice})")
	int insert(FlowNotice flowNotice);
	
	@Update("update sp_flow_notice set recipients = #{recipients}, notice_way = #{noticeWay}, start_notice = #{startNotice}, end_notice = #{endNotice}, exception_notice = #{exceptionNotice} where id = #{id}")
	int update(FlowNotice flowNotice);
	
	@Delete("delete from sp_flow_notice where id = #{id}")
	int deleteById(String id);
}