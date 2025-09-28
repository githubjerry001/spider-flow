package org.spiderflow.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.Function;

import java.util.List;

@Mapper
public interface FunctionMapper {
	
	@Select("select * from sp_function")
	List<Function> selectList();
	
	@Select("select * from sp_function where id = #{id}")
	Function selectById(String id);
	
	@Insert("insert into sp_function(id, name, parameter, script, create_date) values(#{id}, #{name}, #{parameter}, #{script}, #{createDate})")
	int insert(Function function);
	
	@Update("update sp_function set name = #{name}, parameter = #{parameter}, script = #{script}, create_date = #{createDate} where id = #{id}")
	int update(Function function);
	
	@Delete("delete from sp_function where id = #{id}")
	int deleteById(String id);
	
	// 分页查询方法
	@Select("select * from sp_function order by create_date desc limit #{offset}, #{limit}")
	List<Function> selectPage(int offset, int limit);
	
	// 根据名称模糊查询并分页
	@Select("select * from sp_function where name like concat('%', #{name}, '%') order by create_date desc limit #{offset}, #{limit}")
	List<Function> selectPageByName(String name, int offset, int limit);
	
	// 获取总记录数
	@Select("select count(*) from sp_function")
	int selectCount();
	
	// 根据名称模糊查询获取总记录数
	@Select("select count(*) from sp_function where name like concat('%', #{name}, '%')")
	int selectCountByName(String name);
}